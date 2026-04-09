package com.adamstraub.tonsoftacos.services.ownersService.orders;

import com.adamstraub.tonsoftacos.dto.businessDto.OrderItemReturnedToOwnerDTO;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwnerDTO;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessageDTO;
import com.adamstraub.tonsoftacos.entities.Customer;
import com.adamstraub.tonsoftacos.entities.MenuItem;
import com.adamstraub.tonsoftacos.entities.OrderItem;
import com.adamstraub.tonsoftacos.entities.Orders;
import com.adamstraub.tonsoftacos.repository.CustomerRepository;
import com.adamstraub.tonsoftacos.repository.MenuItemRepository;
import com.adamstraub.tonsoftacos.repository.OrderItemRepository;
import com.adamstraub.tonsoftacos.repository.OrdersRepository;
import com.adamstraub.tonsoftacos.services.customerValidationService.CustomerValidationService;
import com.adamstraub.tonsoftacos.services.utilityService.salesService.ISalesService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class OwnersOrdersService implements IOwnersOrdersService {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private CustomerValidationService validationService;
    @Autowired
    private ISalesService salesService;

    @Transactional
    @Override
    public ResponseEntity<List<OrderReturnedToOwnerDTO>> getAllOrders() {
        List<OrderReturnedToOwnerDTO> orderItemDtos = new ArrayList<>();
        List<Orders> orders;
        try {
            orders = ordersRepository.findAll();
            System.out.println(orders);
            for (Orders order : orders) {
                orderItemDtos.add(ownersGetOrderDtoConverter(order));
            }
            System.out.println(orderItemDtos);

        } catch (Exception e) {
        throw new RuntimeException(e);
        }
        return ResponseEntity.ok(orderItemDtos);
    }

    @Transactional
    @Override
    public ResponseEntity<OrderReturnedToOwnerDTO> getOrderByUid(String orderUid) {
        Orders order;
        OrderReturnedToOwnerDTO returnedOrder;
        if(validationService.validateOrderUid(orderUid)) {
            try {
                ordersRepository.findByOrderUid(orderUid);
            } catch (Exception e) {
                throw new EntityNotFoundException("No order found with that UID. Please verify and try again.");
            }
        }
            order = ordersRepository.findByOrderUid(orderUid);
            returnedOrder = ownersGetOrderDtoConverter(order);
            return ResponseEntity.ok(returnedOrder);
    }

    @Transactional
    @Override
    public ResponseEntity<List<OrderReturnedToOwnerDTO>> getOrdersByPhoneNumber(String phone) {
        List<OrderReturnedToOwnerDTO> convertedOrders = new ArrayList<>();
        List<Orders> customerOrders = new ArrayList<>();
        List<Customer> customers;
        if(validationService.validateCustomerPhone(phone)) {
            try {
                customerRepository.findByPhoneNumber(phone);
            } catch (Exception e) {
                throw new EntityNotFoundException("No orders found by that phone number");
            }
        }
        customers = customerRepository.findByPhoneNumber(phone);
            for (Customer customer : customers) {
                customerOrders = ordersRepository.findByCustomerUid(customer.getCustomerUid());
            }
            for ( Orders order : customerOrders) {
                convertedOrders.add(ownersGetOrderDtoConverter(order));
            }
                return ResponseEntity.ok(convertedOrders);
    }

    @Transactional
    @Override
    public ResponseEntity<OrderReturnedToOwnerDTO> orderReady(String orderUid) {
        Orders order;
        try {
            ordersRepository.findByOrderUid(orderUid);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not delete order. Verify order exists.", e);
        }
        order = ordersRepository.findByOrderUid(orderUid);
    String timeReady = new SimpleDateFormat(" MMM-dd-yy hh" +
            ":mm a").format(Calendar.getInstance().getTime());
        order.setReady(timeReady);
        return ResponseEntity.ok(ownersGetOrderDtoConverter(order));
    }

    @Transactional
    @Override
    public ResponseEntity<OrderReturnedToOwnerDTO> closeOrder(String orderUid) {
        Orders order;
        try {
            ordersRepository.findByOrderUid(orderUid);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not delete order. Verify order exists.", e);
        }
        order = ordersRepository.findByOrderUid(orderUid);
    if (order.getReady().equals("no")) {
        throw new IllegalArgumentException("Order can not be closed if order is not ready.");
    }
        String timeClosed = new SimpleDateFormat("dd-MMM-yy hh:mm a").format(Calendar.getInstance().getTime());
    order.setClosed(timeClosed);

    Customer customer = customerRepository.findByCustomerUid(order.getCustomerUid());
    List<Orders> customerOrders = ordersRepository.findByCustomerUid(order.getCustomerUid());
    List<Orders> openOrders = new ArrayList<>();
    for (Orders customerOrder : customerOrders) {
        if (Objects.equals(customerOrder.getClosed(), "no")) {
            openOrders.add(customerOrder);
        }
    }
    if (openOrders.isEmpty()) {
        customer.setName("NA");
        customer.setPhoneNumber("NA");
        customer.setEmail("NA");
        customerRepository.save(customer);
        return ResponseEntity.ok(ownersGetOrderDtoConverter(order));
    }
    return ResponseEntity.ok(ownersGetOrderDtoConverter(order));
}

    @Transactional
    @Override
    public ResponseEntity<ResponseMessageDTO> deleteOrder(String orderUid) {
    Orders order;
    ResponseMessageDTO message = new ResponseMessageDTO();
        try {
            ordersRepository.findByOrderUid(orderUid);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not delete order. Verify order exists.", e);
        }
        order = ordersRepository.findByOrderUid(orderUid);
        ordersRepository.deleteById(order.getOrderId());
        message.setMessage( "Order " + order.getOrderUid() + " deleted.");
    return ResponseEntity.ok(message);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessageDTO> addToOrder(String orderUid, Integer menuItemId, Integer quantity, String itemSize) {
        ResponseMessageDTO message = new ResponseMessageDTO();
        Optional<MenuItem> menuItem;
        try{
            menuItemRepository.getReferenceById(menuItemId);
        }catch (Exception e){
            throw new EntityNotFoundException("Menu item can not be added to order. Verify menu item id.", e);
        }
        try{
            ordersRepository.findByOrderUid(orderUid);
        }catch (Exception e){
            throw new EntityNotFoundException("Menu item can not be added to order. Verify order ", e);
        }
        try {
            OrderItem orderItem = OrderItem.builder()
                    .item(menuItemRepository.getReferenceById(menuItemId))
                    .quantity(quantity)
                    .order(ordersRepository.findByOrderUid(orderUid)).build();
            orderItem.setSize(itemSize);
            orderItem.setTotal(salesService.calcItemPriceWithSize(orderItem.getQuantity(),
                    String.valueOf(orderItem.getSize()),menuItemRepository.getReferenceById(menuItemId).getUnitPrice()
                    ));
            orderItemRepository.save(orderItem);
            Orders order = ordersRepository.findByOrderUid(orderUid);
            order.setOrderTotal(order.getOrderTotal().add(orderItem.getTotal()));
            ordersRepository.save(order);
        } catch (Exception e) {
            log.error("Something unexpected happened while trying to add item to order {}", orderUid, e);
        }
        menuItem = Optional.of(menuItemRepository.getReferenceById(menuItemId));
        message.setMessage( menuItem.get().getItemName() + " x " + quantity + " added to order.");
        return ResponseEntity.ok(message);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessageDTO> removeFromOrder(Integer orderItemId) {
        OrderItem orderItem;
        BigDecimal oldTotal;
        BigDecimal newTotal;
        ResponseMessageDTO message = new ResponseMessageDTO();
        try{
            orderItemRepository.getReferenceById(orderItemId);
        }catch (Exception e){
            throw new EntityNotFoundException("Item does not exist on this order. Please verify order. ", e);
        }
        try {
            orderItem = orderItemRepository.getByOrderItemId(orderItemId);
        } catch (Exception e){
            throw new EntityNotFoundException("Order item cannot be found by it's id. Please contact us", e);
        }

// remove item and update total
        orderItemRepository.deleteById(orderItemId);
        oldTotal = orderItem.getOrder().getOrderTotal();
        newTotal = oldTotal.subtract(orderItem.getTotal());
        Orders orderToEdit = ordersRepository.findByOrderUid(orderItem.getOrder().getOrderUid());
        orderToEdit.setOrderTotal(newTotal);
        ordersRepository.save(orderToEdit);

        message.setMessage("Order item " + orderItem.getItem().getItemName() + " deleted.");
        return ResponseEntity.ok(message);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessageDTO> updateOrderItemQuantity(String orderUid, Integer orderItemId, Integer newQuantity, String newSize) {
        System.out.println("owner orders service");

            ResponseMessageDTO message = new ResponseMessageDTO();

//            validation
        try {
            ordersRepository.findByOrderUid(orderUid);
        } catch (Exception e) {
            throw new EntityNotFoundException(" Verify order exists.", e);
        }
        try {
            orderItemRepository.getByOrderItemId(orderItemId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Order item not updated. Verify order item is part of order.", e);
        }

            if (newQuantity > 10) {
                throw new IllegalArgumentException("We were unable to process your request. " +
                        "Please contact us directly when trying to order more than 10 of any given item.");
            }
            Orders order = ordersRepository.findByOrderUid(orderUid);
        OrderItem orderItem = orderItemRepository.getByOrderItemId(orderItemId);

        //          remove item if total 0
        if(newQuantity == 0){
                orderItemRepository.delete(orderItem);
            order.setOrderTotal(order.getOrderTotal().subtract(orderItem.getTotal()));
                message.setMessage( "Item quantity updated, item removed, order updated.");
            }else{
//            update item
            orderItem.setQuantity(newQuantity);
            orderItem.setSize(newSize);
            order.setOrderTotal(order.getOrderTotal().subtract(orderItem.getTotal()));
            orderItem.setTotal(salesService.calcItemPriceWithSize(orderItem.getQuantity(),
                    orderItem.getSize(),
                    menuItemRepository.getReferenceById(orderItem.getItem().getId()).getUnitPrice()));

            order.setOrderTotal(order.getOrderTotal().add(orderItem.getTotal()));
            orderItemRepository.save(orderItem);
            ordersRepository.save(order);
            message.setMessage( "Order item updated, order updated.");
        }
        return ResponseEntity.ok(message);
    }


    private OrderReturnedToOwnerDTO ownersGetOrderDtoConverter(Orders order) {
        OrderReturnedToOwnerDTO orderReturnedToOwner = new OrderReturnedToOwnerDTO();
        try {
         customerRepository.findByCustomerUid(order.getCustomerUid());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //        set the order dto
        orderReturnedToOwner.setName(customerRepository.findByCustomerUid(order.getCustomerUid()).getName());
        orderReturnedToOwner.setEmail(customerRepository.findByCustomerUid(order.getCustomerUid()).getEmail());
        orderReturnedToOwner.setPhone(customerRepository.findByCustomerUid(order.getCustomerUid()).getPhoneNumber());
        orderReturnedToOwner.setOrderUid(order.getOrderUid());
        orderReturnedToOwner.setCustomerUid(order.getCustomerUid());
//        set the order items dto included in order
        List<OrderItemReturnedToOwnerDTO> orderItemReturnedToOwners = new ArrayList<>();
        List<OrderItem> orderItems = order.getOrderItems();
        orderItems.forEach(orderItem -> orderItemReturnedToOwners.add(ownersOrderItemDtoConvertor(orderItem)));
        orderReturnedToOwner.setOrderItems(orderItemReturnedToOwners);
        orderReturnedToOwner.setOrderTotal(order.getOrderTotal());
        orderReturnedToOwner.setCreated(order.getCreated());
        orderReturnedToOwner.setReady(order.getReady());
        orderReturnedToOwner.setClosed(order.getClosed());
        return orderReturnedToOwner;
    }

    private OrderItemReturnedToOwnerDTO ownersOrderItemDtoConvertor(OrderItem orderItem){
        OrderItemReturnedToOwnerDTO orderItemReturnedToOwner = new OrderItemReturnedToOwnerDTO();

        orderItemReturnedToOwner.setOrderItemId(orderItem.getOrderItemId());
        orderItemReturnedToOwner.setItemName(orderItem.getItem().getItemName());
        orderItemReturnedToOwner.setQuantity(orderItem.getQuantity());
        orderItemReturnedToOwner.setTotal(orderItem.getTotal());
        orderItemReturnedToOwner.setSize(orderItem.getSize());
        return orderItemReturnedToOwner;
    }
}
