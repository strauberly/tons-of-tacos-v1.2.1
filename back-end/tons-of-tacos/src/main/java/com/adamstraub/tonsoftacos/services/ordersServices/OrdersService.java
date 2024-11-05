package com.adamstraub.tonsoftacos.services.ordersServices;
import com.adamstraub.tonsoftacos.dao.MenuItemRepository;
import com.adamstraub.tonsoftacos.dto.customerDto.orderItemsDto.OrderItemReturnedToCustomer;
import com.adamstraub.tonsoftacos.dto.customerDto.orderItemsDto.OrderItemDTO;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.OrderReturnedToCustomer;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.SubmittedOrder;
import com.adamstraub.tonsoftacos.entities.Customer;
import com.adamstraub.tonsoftacos.dao.CustomerRepository;
import com.adamstraub.tonsoftacos.dao.OrdersRepository;
import com.adamstraub.tonsoftacos.entities.OrderItem;
import com.adamstraub.tonsoftacos.entities.Orders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
//import java.util.Map;
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
    Customer checkedCustomer = new Customer();

//    private Map<String, String>

    BigDecimal orderTotal = BigDecimal.valueOf(0.00);


    Orders newOrder = new Orders();

    @Override
    @Transactional

    public OrderReturnedToCustomer createOrder(@RequestBody @NotNull SubmittedOrder order)
    {
        System.out.println("service");
        System.out.println(order);
        OrderReturnedToCustomer customerCopyDto = new OrderReturnedToCustomer();
        Orders orderConfirmation;

        validateCustomerName(order.getCustomer().getName());
        validateCustomerPhone(order.getCustomer().getPhoneNumber());
        validateCustomerEmail(order.getCustomer().getEmail());

        if (!customerNameValid) {
            throw new IllegalArgumentException("Customer name incorrectly formatted. Please consult the documentation.");
        }
        if(!customerPhoneNumberValid) {
             throw new IllegalArgumentException("Customer phone number incorrectly formatted. Please consult the documentation.");
        }
        if (!customerEmailValid){
            throw new IllegalArgumentException("Customer e-mail incorrectly formatted. Please consult the documentation.");
        }
        if (order.getOrder().isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least 1 menu item and must not be null. Please consult the documentation.");
        }

        Customer newCustomer = order.getCustomer();
        try{


        if (customerRepository.findByName(order.getCustomer().getName()) != null &&
                Objects.equals
                        (customerRepository.findByName(order.getCustomer().getName()).getEmail(),
                                order.getCustomer().getEmail())
                && Objects.equals(customerRepository.findByName(order.getCustomer().getName()).getPhoneNumber(),
                order.getCustomer().getPhoneNumber())
        ) {
            newOrder.setCustomerId(customerRepository.findByName(newCustomer.getName()).getCustomerId());
            newOrder.setCustomerUid(customerRepository.findByName(newCustomer.getName()).getCustomerUid());
        } else {
            newCustomer.setCustomerUid(genCustomerUid());
            customerRepository.save(newCustomer);
            newOrder.setCustomerUid(customerRepository.findByName(newCustomer.getName()).getCustomerUid());
            newOrder.setCustomerId(customerRepository.findByCustomerUid(newOrder.getCustomerUid()).getCustomerId());

        }
        }catch (Exception e){
            log.error("e: ", e);
        }

        try{
            newOrder.setOrderItems(submittedOrderItemsConvertor(order.getOrder()));
        } catch (Exception e) {
            log.error("e: ", e);
        }


//set total for each order item before creating the dto and update the grand total
        for (OrderItem orderItem : newOrder.getOrderItems()) {
            char itemSize = orderItem.getSize();

            BigDecimal adjustedUnitPrice = switch (itemSize) {
                case 'm' -> menuItemRepository
                        .getReferenceById(orderItem.getItem().getId()).getUnitPrice().add(BigDecimal.valueOf(0.25));
                case 'l' -> menuItemRepository
                        .getReferenceById(orderItem
                                .getItem().getId())
                        .getUnitPrice()
                        .add(BigDecimal.valueOf(0.50));
                default -> menuItemRepository.getReferenceById(orderItem.getItem().getId()).getUnitPrice();
            };
            orderItem.setTotal(adjustedUnitPrice.multiply(BigDecimal.valueOf(orderItem.getQuantity())));
            System.out.println("item total: " + orderItem.getTotal().toString());
            orderTotal = orderTotal.add(orderItem.getTotal());
        }

//  set order total, customer uid and customer id for new customer
        newOrder.setOrderTotal(orderTotal);
        System.out.println("total: " + newOrder.getOrderTotal().toString());

//set order uid
        newOrder.setOrderUid(genOrderUid());

        try{
            ordersRepository.save(newOrder);
        } catch (Exception e) {
            log.error("e: ", e);
        }

        System.out.println("new order: " + newOrder);

//create an order confirmation
        orderConfirmation = ordersRepository.findByOrderUid(newOrder.getOrderUid());
        customerCopyDto.setCustomerName(customerRepository.findByCustomerUid(orderConfirmation.getCustomerUid()).getName());
        customerCopyDto.setCustomerEmail(customerRepository.findByCustomerUid(orderConfirmation.getCustomerUid()).getEmail());
        customerCopyDto.setCustomerPhone(customerRepository.findByCustomerUid(orderConfirmation.getCustomerUid()).getPhoneNumber());
        customerCopyDto.setOrderUid(newOrder.getOrderUid());
        customerCopyDto.setOrderTotal(newOrder.getOrderTotal());
        List<OrderItemReturnedToCustomer> customerItems = getOrderItemReturnedToCustomers(orderConfirmation);
        customerCopyDto.setOrderItems(customerItems);

        customerNameValid = false;
        customerPhoneNumberValid = false;
        customerEmailValid = false;
        orderTotal = BigDecimal.valueOf(0.00);
        System.out.println(customerCopyDto);
        return customerCopyDto;
    }


    private static @NotNull List<OrderItemReturnedToCustomer> getOrderItemReturnedToCustomers(Orders orderConfirmation) {
        List<OrderItemReturnedToCustomer> customerItems = new ArrayList<>();
        for (OrderItem orderItem : orderConfirmation.getOrderItems()) {
            OrderItemReturnedToCustomer orderItemReturnedToCustomer = new OrderItemReturnedToCustomer();
            orderItemReturnedToCustomer.setItemName(orderItem.getItem().getItemName());
            orderItemReturnedToCustomer.setUnitPrice(orderItem.getItem().getUnitPrice());
            orderItemReturnedToCustomer.setQuantity(orderItem.getQuantity());
            orderItemReturnedToCustomer.setSize(orderItem.getSize());
            orderItemReturnedToCustomer.setTotal(orderItem.getTotal());
            customerItems.add(orderItemReturnedToCustomer);
        }
        return customerItems;
    }


    private List<OrderItem> submittedOrderItemsConvertor(List<OrderItemDTO> orderItems){
        List<OrderItem> items = new ArrayList<>();

        for(OrderItemDTO orderItemDTO : orderItems){
            OrderItem orderItem = new OrderItem();
            char itemSize = orderItemDTO.getSize();

            try{
                orderItem.setItem(menuItemRepository.getReferenceById(Integer.valueOf(orderItemDTO.getMenuId())));
                orderItem.setSize(orderItemDTO.getSize());
                orderItem.setQuantity(orderItemDTO.getQuantity());
                orderItem.setOrder(newOrder);
            } catch (Exception e) {
                log.error("e: " , e);
            }

// adjust item price and total if a size is available and selected
            BigDecimal adjustedUnitPrice = switch (itemSize) {
                case 'm' -> menuItemRepository
                        .getReferenceById(Integer.valueOf(orderItemDTO
                                .getMenuId()))
                        .getUnitPrice()
                        .add(BigDecimal.valueOf(0.25));
                case 'l' -> menuItemRepository
                        .getReferenceById(Integer.valueOf(orderItemDTO
                                .getMenuId()))
                        .getUnitPrice()
                        .add(BigDecimal.valueOf(0.50));
                default -> menuItemRepository.getReferenceById(Integer.valueOf(orderItemDTO.getMenuId())).getUnitPrice();
            };
            orderItem.setTotal(adjustedUnitPrice.multiply(BigDecimal.valueOf(orderItemDTO.getQuantity())));
            items.add(orderItem);
        }
        System.out.println("items: " + items);
        return items;
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
        String customerUidFront;
        String customerUidBack;
        String formattedCustomerUid;
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
