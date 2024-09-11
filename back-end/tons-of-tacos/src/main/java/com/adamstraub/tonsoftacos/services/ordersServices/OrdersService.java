package com.adamstraub.tonsoftacos.services.ordersServices;
import com.adamstraub.tonsoftacos.dao.MenuItemRepository;
import com.adamstraub.tonsoftacos.dao.OrderItemRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.NewOrder;
import com.adamstraub.tonsoftacos.dto.customerDto.orderItemsDto.OrderItemReturnedToCustomer;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.OrderReturnedToCustomer;
import com.adamstraub.tonsoftacos.entities.OrderItem;
import com.adamstraub.tonsoftacos.dao.CustomerRepository;
import com.adamstraub.tonsoftacos.dao.OrdersRepository;
import com.adamstraub.tonsoftacos.entities.Customer;
import com.adamstraub.tonsoftacos.entities.Orders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class OrdersService implements OrdersServiceInterface {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private CustomerRepository customerRepository;

    private boolean customerNameValid = false;
    private boolean customerPhoneNumberValid = false;
    private boolean customerEmailValid = false;

    @Override
    @Transactional
    public OrderReturnedToCustomer createOrder(@RequestBody @NotNull NewOrder order) {
        System.out.println("service");

//        System.out.println("order submitted " + order);

        BigDecimal orderTotal = BigDecimal.valueOf(0.00);
        OrderReturnedToCustomer customerCopyDto = new OrderReturnedToCustomer();

        try {

            Orders newOrder = order.getOrder();

            System.out.println(newOrder.toString());

            Orders orderConfirmation;

            List<OrderItem> receivedOrderItems = newOrder.getOrderItems();

            List<OrderItemReturnedToCustomer> orderItemDtos = new ArrayList<>();

//  validation
            validateCustomerName(order.getCustomer().getName());
            validateCustomerPhone(order.getCustomer().getPhoneNumber());
            validateCustomerEmail(order.getCustomer().getEmail());


            if (!customerNameValid) {
                throw new IllegalArgumentException("Customer name incorrectly formatted. Please consult the documentation.");
            }
            if (!customerPhoneNumberValid) {
                throw new IllegalArgumentException("Customer phone number incorrectly formatted. Please consult the documentation.");
            }
            if (!customerEmailValid) {
                throw new IllegalArgumentException("Customer e-mail incorrectly formatted. Please consult the documentation.");
            }
            if (order.getOrder().getOrderItems().isEmpty()) {
                throw new IllegalArgumentException("An order must contain at least 1 menu item and must not be null. Please consult the documentation.");
            }

//  if customer already exists, use existing customer id else create new customer
            Customer newCustomer = order.getCustomer();
            if (customerRepository.findByName(newCustomer.getName()) != null &&
                    Objects.equals
                            (customerRepository.findByName(newCustomer.getName()).getEmail(),
                                    order.getCustomer().getEmail())
                    && Objects.equals(customerRepository.findByName(newCustomer.getName()).getPhoneNumber(),
                    order.getCustomer().getPhoneNumber())
            ) {
                newOrder.setCustomerId(customerRepository.findByName(newCustomer.getName()).getCustomerId());
                newOrder.setCustomerUid(customerRepository.findByName(newCustomer.getName()).getCustomerUid());
            } else {
                newCustomer.setCustomerUid(genCustomerUid());
                customerRepository.save(newCustomer);
                newCustomer = customerRepository.findByName(newCustomer.getName());
            }

//            instantiate orderitems so they can be worked with
            newOrder.setOrderItems(receivedOrderItems);

//            set total for each order item before creating the dto and update the grand total
//        will most likely need to outsource to another method or class and create library (something like if category drink m=.25 l=.50, if category side m=.75 and l=1.00)
            for (OrderItem orderItem : receivedOrderItems) {
                char itemSize = orderItem.getSize();

                BigDecimal adjustedUnitPrice = switch (itemSize) {
                    case 'm' -> menuItemRepository
                            .getReferenceById(orderItem
                                    .getItem()
                                    .getId())
                            .getUnitPrice()
                            .add(BigDecimal.valueOf(0.25));
                    case 'l' -> menuItemRepository
                            .getReferenceById(orderItem
                                    .getItem()
                                    .getId())
                            .getUnitPrice()
                            .add(BigDecimal.valueOf(0.50));
                    default -> menuItemRepository.getReferenceById(orderItem.getItem().getId()).getUnitPrice();
                };

                orderItem.setTotal(adjustedUnitPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity())));
                orderTotal = orderTotal.add(orderItem.getTotal());
            }
//  set order total, customer uid and customer id for new customer
            newOrder.setOrderTotal(orderTotal);
            newOrder.setCustomerUid(newCustomer.getCustomerUid());
            newOrder.setCustomerId(newCustomer.getCustomerId());

//  set order uid
            newOrder.setOrderUid(genOrderUid());
            ordersRepository.save(newOrder);

//        reset validation flags
            customerNameValid = false;
            customerPhoneNumberValid = false;
            customerEmailValid = false;

//create an order confirmation
            orderConfirmation = ordersRepository.findByOrderUid(newOrder.getOrderUid());
            customerCopyDto.setCustomerName(newCustomer.getName());
            customerCopyDto.setOrderUid(newOrder.getOrderUid());
            customerCopyDto.setOrderTotal(newOrder.getOrderTotal());

            receivedOrderItems = orderConfirmation.getOrderItems();
            for (OrderItem orderItem : receivedOrderItems) {
                orderItemDtos.add(orderItemDtoConvertor(orderItem));
            }
            customerCopyDto.setOrderItems(orderItemDtos);
        } catch (Exception e) {
            log.error("e: ", e);
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        return customerCopyDto;
    }



    private OrderItemReturnedToCustomer orderItemDtoConvertor(OrderItem orderItem) {
        OrderItemReturnedToCustomer orderItemDto = new OrderItemReturnedToCustomer();

        orderItemDto.setItemName(menuItemRepository.getReferenceById(orderItem.getItem().getId()).getItemName());
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setUnitPrice(orderItem.getTotal().divide(BigDecimal.valueOf(orderItem.getQuantity()), 2, RoundingMode.UP));
        orderItemDto.setTotal(orderItem.getTotal());
        orderItemDto.setSize(orderItem.getSize());
        System.out.println(orderItemDto);
        return orderItemDto;
    }

    private String genOrderUid() {
        // desired result example: 11A32
        String orderUid = null;
        StringBuilder orderUidBuilder = new StringBuilder(5);

        for (int i = 0; i < 5; i++) {
            orderUid = String.valueOf(orderUidBuilder.append(randomUidChar()));

        }
//        ensure uid is unique
//        compare uid against others by doing a find by uid and if null then return if not re-run
        if (ordersRepository.findByOrderUid(orderUid) != null) {
            genOrderUid();
        }
        return orderUid;
    }

    private String genCustomerUid() {
        // desired result example: 11A3-ewr3
        String customerUid = null;
        String customerUidFront = null;
        String customerUidBack = null;
        String formattedCustomerUid = null;
        StringBuilder orderUidBuilder = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            customerUid = String.valueOf(orderUidBuilder.append(randomUidChar()));
        }
            customerUidFront = customerUid.substring(0,4);
            customerUidBack = customerUid.substring(4);
            formattedCustomerUid = customerUidFront + "-" + customerUidBack;

//        compare uid against others by doing a find by uid and if null then return if not re-run
        if (customerRepository.findByCustomerUid(customerUid) != null) {
            genCustomerUid();
        }
        return formattedCustomerUid;
    }

    private char randomUidChar() {
        int min = 48, max = 90;
        int random = (int) (Math.random() * ((max - min)) + min);
        char randomChar;
        int[] excluded = {58, 59, 60, 61, 62, 63, 64};
        if (ArrayUtils.contains(excluded, random)) {
            randomChar = randomUidChar();
            return randomChar;
        }
        return (char) random;
    }

    private void validateCustomerName(String customerName) {
        byte[] nameChars = customerName.getBytes(StandardCharsets.UTF_8);
        int spaces = 0;
        for (Byte nameChar : nameChars) {
            if (Objects.equals(nameChar, (byte) 32)) {
                spaces += 1;
            }
        }
//        possibly alter for just ^[a-zA-Z]$+ [a-zA-Z]+. currently accepting letters from any language.
        if (customerName.matches("^\\p{L}+[\\p{L}\\p{Pd}\\p{Zs}']*\\p{L}+$|^\\p{L}+$") &&
                    spaces == 1) {
                customerNameValid = true;
        }
    }

    private void validateCustomerPhone(String customerPhone){
        if (customerPhone.matches("[0-9.]*")
                && customerPhone.charAt(3) == (char) 46
                && customerPhone.charAt(7) == (char) 46
                && customerPhone.length()==12){
            customerPhoneNumberValid = true;
        }
    }
    private void validateCustomerEmail(String customerEmail){
        if (customerEmail.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,}")) customerEmailValid = true;
    }

}
