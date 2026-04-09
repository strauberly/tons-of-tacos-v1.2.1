package com.adamstraub.tonsoftacos.services.ordersService;
import com.adamstraub.tonsoftacos.repository.MenuItemRepository;
import com.adamstraub.tonsoftacos.dto.customerDto.orderItemDto.OrderItemReturnedToCustomerDTO;
import com.adamstraub.tonsoftacos.dto.customerDto.orderItemDto.OrderItemDTO;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.OrderReturnedToCustomerDTO;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.SubmittedOrderDTO;
import com.adamstraub.tonsoftacos.entities.Customer;
import com.adamstraub.tonsoftacos.repository.CustomerRepository;
import com.adamstraub.tonsoftacos.repository.OrdersRepository;
import com.adamstraub.tonsoftacos.entities.OrderItem;
import com.adamstraub.tonsoftacos.entities.Orders;
import com.adamstraub.tonsoftacos.services.customerValidationService.CustomerValidationService;
import com.adamstraub.tonsoftacos.services.utilityService.salesService.ISalesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
public class OrdersService implements IOrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
   private CustomerValidationService customerValidationService;
    @Autowired
    private ISalesService salesService;

    private boolean newCustomerFlag = false;
    private final OrderReturnedToCustomerDTO customerCopyDto = new OrderReturnedToCustomerDTO();
    private Customer existingCustomer = new Customer();

    @Transactional
    @Override
    public ResponseEntity<OrderReturnedToCustomerDTO> createOrder(@RequestBody @NotNull SubmittedOrderDTO order)
    {
        Orders newOrder = new Orders();
        Customer newCustomer = order.getCustomer();
        try{
            customerValidationService.validateCustomerName(order.getCustomer().getName());
            customerValidationService.validateCustomerPhone(order.getCustomer().getPhoneNumber());
            customerValidationService.validateCustomerEmail(order.getCustomer().getEmail());
        if (order.getOrder().isEmpty()) {
            throw new IllegalArgumentException("An order must contain at least 1 menu item and must not be null. Please consult the documentation.");
        }
            checkIfCustomerExists(order.getCustomer());
            prepareCustomerInfo(newCustomer, newOrder);

//prepare order items
    newOrder.setOrderItems(submittedOrderItemsConvertor(Collections.unmodifiableList(order.getOrder()), newOrder));
    totalOrder(newOrder);
    newOrder.setOrderUid(genOrderUid());

//    save order and create customer confirmation
    ordersRepository.save(newOrder);
    setOrderConfirmation(newCustomer, newOrder);
} catch (Exception e) {
            log.error("error: ", e);
}
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerCopyDto);
    }

    private void setOrderConfirmation(Customer newCustomer, Orders newOrder){
        try{
            Orders orderConfirmation = ordersRepository.findByOrderUid(newOrder.getOrderUid());
        customerCopyDto.setCustomerName(newCustomer.getName());
        customerCopyDto.setCustomerEmail(newCustomer.getEmail());
        customerCopyDto.setCustomerPhone(newCustomer.getPhoneNumber());
        customerCopyDto.setOrderUid(newOrder.getOrderUid());
        customerCopyDto.setOrderTotal(newOrder.getOrderTotal());
        List<OrderItemReturnedToCustomerDTO> customerItems = getOrderItemReturnedToCustomers(orderConfirmation);
        customerCopyDto.setOrderItems(customerItems);
        } catch (Exception e) {
            log.error("error: ", e);
        }
    }

// customer prep
    private void checkIfCustomerExists(Customer submittedCustomer){
        try{
            List<Customer> queriedCustomers = customerRepository.findByNameContaining(submittedCustomer.getName());
            List<Customer> allCustomers = customerRepository.findAll();
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
        }catch (Exception e){
            log.error("error: ", e);
        }
    }

    private void prepareCustomerInfo(Customer customer, Orders newOrder){
        try{
        if (newCustomerFlag){
            customer.setCustomerUid(genCustomerUid());
            customerRepository.save(customer);
            newOrder.setCustomerUid(customer.getCustomerUid());
        }else {
            newOrder.setCustomerUid(existingCustomer.getCustomerUid());
        }
        } catch (Exception e) {
            log.error("error: " , e);
        }
    }

//    operations
    private void totalOrder(Orders newOrder){
         BigDecimal orderTotal = BigDecimal.valueOf(0.00);
        try{
        for (OrderItem orderItem : newOrder.getOrderItems()) {
            orderItem.setTotal(orderItem.getTotal());
            System.out.println("item total: " + orderItem.getTotal().toString());
            orderTotal = orderTotal.add(orderItem.getTotal());
        }
        newOrder.setOrderTotal(orderTotal);
    } catch (Exception e) {
        log.error("error: " , e);
    }

    }

    private static @NotNull List<OrderItemReturnedToCustomerDTO> getOrderItemReturnedToCustomers(Orders orderConfirmation) {
        List<OrderItemReturnedToCustomerDTO> customerItems = new ArrayList<>();
        try{
        for (OrderItem orderItem : orderConfirmation.getOrderItems()) {
            OrderItemReturnedToCustomerDTO orderItemReturnedToCustomer = new OrderItemReturnedToCustomerDTO();
            orderItemReturnedToCustomer.setItemName(orderItem.getItem().getItemName());
            orderItemReturnedToCustomer.setUnitPrice(orderItem.getItem().getUnitPrice());
            orderItemReturnedToCustomer.setQuantity(orderItem.getQuantity());
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
                    orderItem.setItem(menuItemRepository.getReferenceById(Integer.valueOf(orderItemDTO.getMenuId())));
                    orderItem.setSize(orderItemDTO.getSize());
                    orderItem.setQuantity(orderItemDTO.getQuantity());
                    orderItem.setOrder(newOrder);

                    orderItem.setTotal(salesService.calcItemPriceWithSize(orderItemDTO.getQuantity(),
                            orderItemDTO.getSize(),
                            menuItemRepository.getReferenceById(Integer.valueOf(orderItemDTO.getMenuId())).getUnitPrice()));
                    items.add(orderItem);
            }
        } catch (Exception e) {
            log.error("error: ", e);
        }
        return items;
    }

//    uid gen  // desired result example: 11A32
    private String genOrderUid() {
        String orderUid = null;
        StringBuilder orderUidBuilder = new StringBuilder(5);
        try{
            for (int i = 0; i < 5; i++) {
                orderUid = String.valueOf(orderUidBuilder.append(randomUidChar()));

            }
/*        ensure uid is unique compare uid against others by doing
            a find by uid and if null then return if not re-run
 */
            if (ordersRepository.findByOrderUid(orderUid) != null) {
                genOrderUid();
            }
        } catch (Exception e) {
            log.error("error: " , e);
        }
        return orderUid;
    }

    // desired result example: 11A3-ewr3
    private String genCustomerUid() {
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
        try{
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
