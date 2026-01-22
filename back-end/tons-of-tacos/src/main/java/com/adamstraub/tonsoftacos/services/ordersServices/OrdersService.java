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
import java.util.Collections;
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
    private boolean newCustomerFlag = false;
//    private BigDecimal orderTotal = BigDecimal.valueOf(0.00);
//    chase and fix persistance
//    private final Orders newOrder = new Orders();
    private final OrderReturnedToCustomer customerCopyDto = new OrderReturnedToCustomer();
    private Customer existingCustomer = new Customer();

    @Override
    @Transactional

    public OrderReturnedToCustomer createOrder(@RequestBody @NotNull SubmittedOrder order)
    {
        System.out.println("service");
        System.out.println(order);
        Orders newOrder = new Orders();
        Customer newCustomer = new Customer();
        newCustomer = order.getCustomer();


        try{


        validateCustomerInfo(order);
        if (order.getOrder().isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least 1 menu item and must not be null. Please consult the documentation.");
        }

            checkIfCustomerExists(order.getCustomer());
            prepareCustomerInfo(newCustomer, newOrder);

////prepare order items
    newOrder.setOrderItems(submittedOrderItemsConvertor(Collections.unmodifiableList(order.getOrder()), newOrder));
    totalOrder(newOrder);
    newOrder.setOrderUid(genOrderUid());
            System.out.println("new order: " + newOrder);

    ordersRepository.save(newOrder);
        System.out.println("order saved");

    setOrderConfirmation(newCustomer, newOrder);
            System.out.println("customer copy: " + customerCopyDto);


} catch (Exception e) {
            log.error("error: ", e);
}
        return customerCopyDto;
    }

    private void setOrderConfirmation(Customer newCustomer, Orders newOrder){
        try{
            Orders orderConfirmation = ordersRepository.findByOrderUid(newOrder.getOrderUid());
        customerCopyDto.setCustomerName(newCustomer.getName());
        customerCopyDto.setCustomerEmail(newCustomer.getEmail());
        customerCopyDto.setCustomerPhone(newCustomer.getPhoneNumber());
        customerCopyDto.setOrderUid(newOrder.getOrderUid());
        customerCopyDto.setOrderTotal(newOrder.getOrderTotal());
        List<OrderItemReturnedToCustomer> customerItems = getOrderItemReturnedToCustomers(orderConfirmation);
        customerCopyDto.setOrderItems(customerItems);
        } catch (Exception e) {
            log.error("error: ", e);
        }
    }
// customer validation and preparation
    private void validateCustomerInfo(SubmittedOrder submittedOrder){
        try{

        validateCustomerName(submittedOrder.getCustomer().getName());
        validateCustomerPhone(submittedOrder.getCustomer().getPhoneNumber());
        validateCustomerEmail(submittedOrder.getCustomer().getEmail());

        if(customerNameValid && customerEmailValid && customerPhoneNumberValid){
            System.out.println("Customer info valid.");
        }
        } catch (Exception e) {
            log.error("error: " , e);
        }
    }

    private void validateCustomerName(String customerName) {
        try{
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
        if (!customerNameValid) {
            throw new IllegalArgumentException("Customer name incorrectly formatted. Please consult the documentation.");
        }
        } catch (Exception e) {
            log.error("error: " , e);
        }
    }

    private void validateCustomerPhone(String customerPhone){
        try{
        if (customerPhone.matches("[0-9.]*")
                && customerPhone.charAt(3) == (char) 46
                && customerPhone.charAt(7) == (char) 46
                && customerPhone.length()==12){
            customerPhoneNumberValid = true;
        }
        if(!customerPhoneNumberValid) {
            throw new IllegalArgumentException("Customer phone number incorrectly formatted. Please consult the documentation.");
        }
        } catch (Exception e) {
            log.error("error: " , e);
        }
    }

    private void validateCustomerEmail(String customerEmail){
        try{


        if (customerEmail.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,}")) customerEmailValid = true;
        if (!customerEmailValid){
            throw new IllegalArgumentException("Customer e-mail incorrectly formatted. Please consult the documentation.");
        }
        } catch (Exception e) {
            log.error("error: " , e);
        }
    }

    private void checkIfCustomerExists(Customer submittedCustomer){
        try{
            List<Customer> queriedCustomers = customerRepository.findByNameContaining(submittedCustomer.getName());
            List<Customer> allCustomers = customerRepository.findAll();
            System.out.println("customer: " + submittedCustomer);
            for (Customer customer : allCustomers)
                if (Objects.equals(submittedCustomer.getName(), customer.getName())) {
                    for (Customer queriedCustomer : queriedCustomers) {
                        if (submittedCustomer.getEmail().equals(queriedCustomer.getEmail()) ||
                                submittedCustomer.getPhoneNumber().equals(queriedCustomer.getPhoneNumber())) {
                            existingCustomer = queriedCustomer;
                            newCustomerFlag = false;
                            break;
                        }else{
                            newCustomerFlag = true;
                        }
                    }
                } else {
                    newCustomerFlag = true;
                }
            System.out.println("new customer: " + newCustomerFlag);
        }catch (Exception e){
            log.error("error: ", e);
        }
    }

    private void prepareCustomerInfo(Customer customer, Orders newOrder){
        try{
        if (newCustomerFlag){
            customer.setCustomerUid(genCustomerUid());
            System.out.println(customer);
            customerRepository.save(customer);
            System.out.println("saved customer");
            newOrder.setCustomerUid(customer.getCustomerUid());
//            newOrder.setCustomerId(customerRepository.findByCustomerUid(customer.getCustomerUid()).getCustomerId());
            System.out.println("customer set to order");
        }else {
            System.out.println("existing customer: " + existingCustomer);
//            newOrder.setCustomerId(existingCustomer.getCustomerId());
            newOrder.setCustomerUid(existingCustomer.getCustomerUid());
            System.out.println("customer set to order");
            System.out.println("new order: " + newOrder);
        }
        } catch (Exception e) {
            log.error("error: " , e);
        }
    }

//    operations
    private void totalOrder(Orders newOrder){
         BigDecimal orderTotal = BigDecimal.valueOf(0.00);
        try{
//
        for (OrderItem orderItem : newOrder.getOrderItems()) {
//
//
//
////
            orderItem.setTotal(orderItem.getTotal());

            System.out.println("item total: " + orderItem.getTotal().toString());
            orderTotal = orderTotal.add(orderItem.getTotal());
        }
        newOrder.setOrderTotal(orderTotal);
        System.out.println("total: " + newOrder.getOrderTotal().toString());
    } catch (Exception e) {
        log.error("error: " , e);
    }

    }

    private static @NotNull List<OrderItemReturnedToCustomer> getOrderItemReturnedToCustomers(Orders orderConfirmation) {
        List<OrderItemReturnedToCustomer> customerItems = new ArrayList<>();
        try{
        for (OrderItem orderItem : orderConfirmation.getOrderItems()) {
            OrderItemReturnedToCustomer orderItemReturnedToCustomer = new OrderItemReturnedToCustomer();
            orderItemReturnedToCustomer.setItemName(orderItem.getItem().getItemName());
            orderItemReturnedToCustomer.setUnitPrice(orderItem.getItem().getUnitPrice());
            orderItemReturnedToCustomer.setQuantity(orderItem.getQuantity());
//            orderItemReturnedToCustomer.setSize(orderItem.getSize());
            orderItemReturnedToCustomer.setSize(orderItem.getSize());
            orderItemReturnedToCustomer.setTotal(orderItem.getTotal());
            customerItems.add(orderItemReturnedToCustomer);
        }
        } catch (Exception e) {
            log.error("error: " , e);
        }
        return customerItems;
    }

    private List<OrderItem> submittedOrderItemsConvertor(List<OrderItemDTO> orderItems, Orders newOrder) {
        List<OrderItem> items = new ArrayList<>();
        try {
            for (OrderItemDTO orderItemDTO : orderItems) {
                OrderItem orderItem = new OrderItem();
//                char itemSize = orderItemDTO.getSize();

                String itemSize = orderItemDTO.getSize();


                    orderItem.setItem(menuItemRepository.getReferenceById(Integer.valueOf(orderItemDTO.getMenuId())));
                    orderItem.setSize(orderItemDTO.getSize());
                    orderItem.setQuantity(orderItemDTO.getQuantity());
                    orderItem.setOrder(newOrder);


// adjust item price and total if a size is available and selected
//                this should be an reuseable method
                    BigDecimal adjustedUnitPrice = switch (itemSize) {
                        case "M" -> menuItemRepository
                                .getReferenceById(Integer.valueOf(orderItemDTO
                                        .getMenuId()))
                                .getUnitPrice()
                                .add(BigDecimal.valueOf(0.50));
                        case "L" -> menuItemRepository
                                .getReferenceById(Integer.valueOf(orderItemDTO
                                        .getMenuId()))
                                .getUnitPrice()
                                .add(BigDecimal.valueOf(1.00));
                        default ->
                                menuItemRepository.getReferenceById(Integer.valueOf(orderItemDTO.getMenuId())).getUnitPrice();
                    };
                    orderItem.setTotal(adjustedUnitPrice.multiply(BigDecimal.valueOf(orderItemDTO.getQuantity())));
                    items.add(orderItem);

            }
        } catch (Exception e) {
            log.error("error: ", e);
        }
                System.out.println("items: " + items);
                return items;
    }

//    uid gen
    private String genOrderUid() {
        // desired result example: 11A32
        String orderUid = null;
        StringBuilder orderUidBuilder = new StringBuilder(5);
        try{
            for (int i = 0; i < 5; i++) {
                orderUid = String.valueOf(orderUidBuilder.append(randomUidChar()));

            }
//        ensure uid is unique
//        compare uid against others by doing a find by uid and if null then return if not re-run
            if (ordersRepository.findByOrderUid(orderUid) != null) {
                genOrderUid();
            }
        } catch (Exception e) {
            log.error("error: " , e);
        }
        return orderUid;
    }

    private String genCustomerUid() {
        String customerUid = null;
        String customerUidFront;
        String customerUidBack;
        String formattedCustomerUid;
        StringBuilder orderUidBuilder = new StringBuilder(8);

        // desired result example: 11A3-ewr3

        for (int i = 0; i < 8; i++) {
            customerUid = String.valueOf(orderUidBuilder.append(randomUidChar()));
        }
            customerUidFront = customerUid.substring(0,4);
            customerUidBack = customerUid.substring(4);
            formattedCustomerUid = customerUidFront + "-" + customerUidBack;
        try{
//        compare uid against others by doing a find by uid and if null then return if not re-run
        if (customerRepository.findByCustomerUid(customerUid) != null) {
            genCustomerUid();
        }
        } catch (Exception e) {
            log.error("error: " , e);
        }
        return formattedCustomerUid;
    }

    private char randomUidChar() {

            int min = 48, max = 90;
            int random = (int) (Math.random() * ((max - min)) + min);
            char randomChar;
            int[] excluded = {58, 59, 60, 61, 62, 63, 64};
        try{
            if (ArrayUtils.contains(excluded, random)) {
                randomChar = randomUidChar();
                return randomChar;
            }
        } catch (Exception e) {
            log.error("error: " , e);
        }
        return (char) random;
    }
}
