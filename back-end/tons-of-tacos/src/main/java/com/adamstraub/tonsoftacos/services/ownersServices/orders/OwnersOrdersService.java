package com.adamstraub.tonsoftacos.services.ownersServices.orders;
import com.adamstraub.tonsoftacos.dao.MenuItemRepository;
import com.adamstraub.tonsoftacos.dao.OrderItemRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.DailySales;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import com.adamstraub.tonsoftacos.entities.MenuItem;
import com.adamstraub.tonsoftacos.entities.OrderItem;
import com.adamstraub.tonsoftacos.entities.Orders;
import com.adamstraub.tonsoftacos.dao.CustomerRepository;
import com.adamstraub.tonsoftacos.dao.OrdersRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwner;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderItemReturnedToOwner;
import com.adamstraub.tonsoftacos.entities.Customer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class OwnersOrdersService implements OwnersOrdersServiceInterface {
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OrderReturnedToOwner> getAllOrders() {
        System.out.println("service");
        List<OrderReturnedToOwner> orderItemDtos = new ArrayList<>();
        List<Orders> orders;

        try {
            orders = ordersRepository.findAll();
            System.out.println(orders);
            for (Orders order : orders) {
                orderItemDtos.add(ownersGetOrderDtoConverter(order));
            }
            System.out.println(orderItemDtos);

        } catch (Exception e) {
//            throw new EntityNotFoundException("No orders found at all. Please contact your application team right away.");
            System.out.println(e);
        }

        return orderItemDtos;
    }

    @Override
    public OrderReturnedToOwner getOrderByUid(String orderUid) {
        System.out.println("service");
        Orders order;

        try {
            order = ordersRepository.findByOrderUid(orderUid);
//            create order DTO and return
            ownersGetOrderDtoConverter(order);
            return ownersGetOrderDtoConverter(order);
        }catch (Exception e) {
            throw new EntityNotFoundException("No order found with that UID. Please verify and try again.");
        }
    }

    @Override
    public List<OrderReturnedToOwner> getOpenOrderByCustomer(String customer) {
        System.out.println("service");
        Customer customerObj;
        List<Orders> orders;
        try {
            customerObj = customerRepository.findByName(customer);
        }catch (Exception e){
            throw new EntityNotFoundException("Customer not found.");
        }
        try {
            orders = ordersRepository.findByCustomerId(customerObj.getCustomerId());
        } catch (Exception e){
            throw new EntityNotFoundException("No orders for customer found.");
        }
        List<OrderReturnedToOwner> openOrders = new ArrayList<>();
        for (Orders order: orders)
            if (order.getClosed().equals("no")) {
            openOrders.add(ownersGetOrderDtoConverter(order));
            }
        if (!openOrders.isEmpty()) {
            return openOrders;
        }else{
            throw new EntityNotFoundException("No open orders for customer found");
        }
    }
@Transactional
    @Override
public OrderReturnedToOwner orderReady(String orderUid) {
        System.out.println("service");
        Orders order;
            order = ordersRepository.findByOrderUid(orderUid);
            if (order == null){
                throw new EntityNotFoundException("Order does not exist. Please verify order id and try again.");
            }

        String timeReady = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        order.setReady(timeReady);
        System.out.println("Order up!");
        return ownersGetOrderDtoConverter(order);
    }
@Transactional
    @Override
public OrderReturnedToOwner closeOrder(String orderUid) {
        System.out.println("service");
        Orders order;
            order = ordersRepository.findByOrderUid(orderUid);
            if (order == null){
                throw new EntityNotFoundException("Order cannot be found and as such can not be closed. Please verify order id.");
            }

        if (order.getReady().equals("no")){
            throw new IllegalArgumentException("Order can not be closed if order is not ready.");
        }
        String timeClosed = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        order.setClosed(timeClosed);

    Customer customer = customerRepository.getReferenceById(order.getCustomerId());
        List<Orders> customerOrders = customer.getOrders();
        List<Orders> openOrders = new ArrayList<>();
        for (Orders customerOrder : customerOrders) {
            if (customerOrder.getClosed().equals("open")){
                openOrders.add(customerOrder);
            }
        }
        if (openOrders.isEmpty()){
            customerRepository.deleteById(customer.getCustomerId());
        }
        return ownersGetOrderDtoConverter(order);
    }
@Transactional
    @Override

public ResponseMessage deleteOrder(String orderUid) {
    System.out.println(orderUid);
        System.out.println("service");
        Orders order;
    ResponseMessage message = new ResponseMessage();
        order = ordersRepository.findByOrderUid(orderUid);

        if(order == null){
            throw new EntityNotFoundException("Can not delete order. Verify order exists.");
        }

        ordersRepository.deleteById(order.getOrderId());
        message.setMessage( "Order " + order.getOrderUid() + " deleted.");
    System.out.println(message);
    return message;
    }

    @Override
    public ResponseMessage addToOrder(String orderUid, Integer menuItemId, Integer quantity, String itemSize) {
        System.out.println(orderUid + " " + menuItemId + " " + quantity + " " + itemSize);
        System.out.println("service");
        ResponseMessage message = new ResponseMessage();
//        System.out.println(orderUid + " " + menuItemId + " " + quantity);
        Optional<MenuItem> menuItem = Optional.of(new MenuItem());
        System.out.println(menuItem);
        Optional<Orders> orderToUpdate = Optional.of(new Orders());
        System.out.println(orderToUpdate);



        try{
            menuItem = Optional.of(menuItemRepository.getReferenceById(menuItemId));
            System.out.println(menuItem);
        }catch (Exception e){
            throw new EntityNotFoundException("Menu item can not be added to order. Verify menu item id.");
        }
        try{
            orderToUpdate = Optional.of(ordersRepository.findByOrderUid(orderUid));
            System.out.println(orderToUpdate);
        }catch (Exception e){
            throw new EntityNotFoundException("Menu item can not be added to order. Verify order id.");
        }
        try {
            OrderItem orderItem = OrderItem.builder()
                    .item(menuItemRepository.getReferenceById(menuItemId))
                    .quantity(quantity)
                    .order(ordersRepository.findByOrderUid(orderUid)).build();
//            calc total
//            orderItem.setTotal(BigDecimal.valueOf(orderItem.getQuantity()).multiply(menuItemRepository.getReferenceById(menuItemId).getUnitPrice()));

            orderItem.setSize(itemSize.charAt(0));
            orderItem.setTotal(calcPrice(orderItem.getQuantity(), String.valueOf(orderItem.getSize()), menuItemRepository.getReferenceById(menuItemId).getUnitPrice()));
            System.out.println("order item: " + orderItem);
            orderItemRepository.save(orderItem);

            Orders order = ordersRepository.findByOrderUid(orderUid);

            order.setOrderTotal(order.getOrderTotal().add(orderItem.getTotal()));
            System.out.println(order);

            ordersRepository.save(order);
        } catch (Exception e) {
            System.out.println(e);
        }
        message.setMessage( menuItem.get().getItemName() + " x " + quantity + " added to order.");
        System.out.println(message);
        return message;
    }

    @Override
    public ResponseMessage removeFromOrder(Integer orderItemId) {
        System.out.println(orderItemId);
        System.out.println("service");
        OrderItem orderItem;
        BigDecimal oldTotal;
        BigDecimal newTotal;

        ResponseMessage message = new ResponseMessage();
        orderItem = orderItemRepository.getByOrderItemId(orderItemId);
        System.out.println(orderItem);

        if(orderItem == null){
            throw new EntityNotFoundException("Can not delete order item. Verify order item exists.");
        }
        orderItemRepository.deleteById(orderItemId);
// update order total
        oldTotal = orderItem.getOrder().getOrderTotal();
        System.out.println(oldTotal);
        newTotal = oldTotal.subtract(orderItem.getTotal());
        System.out.println(newTotal);
        Orders orderToEdit = ordersRepository.findByOrderUid(orderItem.getOrder().getOrderUid());
        orderToEdit.setOrderTotal(newTotal);
        ordersRepository.save(orderToEdit);

        message.setMessage("Order item " + orderItem.getItem().getItemName() + " deleted.");
        return message;
    }

    @Transactional
    @Override
    public ResponseMessage updateOrderItemQuantity(String orderUid, Integer orderItemId, Integer newQuantity) {
            System.out.println("service");
            ResponseMessage message = new ResponseMessage();
//            validation
        Orders order = ordersRepository.findByOrderUid(orderUid);
        if (order == null){
            throw new EntityNotFoundException("Order item not updated. Verify order exists.");
        }
        OrderItem orderItem = orderItemRepository.getByOrderItemId(orderItemId);
        if (orderItem == null){
            throw new EntityNotFoundException("Order item not updated. Verify order item is part of order.");
        }

            if (newQuantity > 10) {
                throw new IllegalArgumentException("We were unable to process your request. " +
                        "Please contact us directly when trying to order more than 10 of any given item.");
            }
//          remove item if total 0
        if(newQuantity == 0){
                orderItemRepository.delete(orderItem);
            order.setOrderTotal(order.getOrderTotal().subtract(orderItem.getTotal()));
                message.setMessage( "Item quantity updated, item removed, order updated.");
            }else{
            orderItem.setQuantity(newQuantity);
            orderItem.setTotal(menuItemRepository.getReferenceById(orderItem.getItem().getId()).getUnitPrice().multiply(
                    BigDecimal.valueOf(orderItem.getQuantity())));
            order.setOrderTotal(order.getOrderTotal().add(orderItem.getTotal()));
            orderItemRepository.save(orderItem);
            ordersRepository.save(order);
            message.setMessage( "Order item updated, order updated.");
        }
        return message;
    }


    @Transactional
    @Override
    public DailySales todaysSales() {
        System.out.println("service");
        DailySales salesToday = new DailySales();
        String formattedSales;
        LocalDate todaysDate = LocalDate.now();
        LocalDate dbDate;
        DateTimeFormatter formattedDate = DateTimeFormatter.ofPattern("dd MMM yyyy");
        BigDecimal salesTotal = BigDecimal.valueOf(0.00);
        List<Orders> todaysOrders = new ArrayList<>();

//        find orders by if they closed meaning transaction complete
        List<Orders> completedOrders = ordersRepository.findByClosed();
        for (Orders completedOrder: completedOrders){
            dbDate = completedOrder.getCreated().toLocalDateTime().toLocalDate();
            if(todaysDate.format(formattedDate).equals(dbDate.format(formattedDate))){
                todaysOrders.add(completedOrder);
            }
        }
//                looks for all orders with today's timestamp
        for (Orders order:todaysOrders){
            salesTotal = salesTotal.add(new BigDecimal(String.valueOf(order.getOrderTotal())));
        }

//        calculate todays sales and return a formatted response
        int numberOfOrders = todaysOrders.size();
        salesToday.setDate(todaysDate);
        salesToday.setNumberOfSales(todaysOrders.size());
        salesToday.setTotal(salesTotal);

        formattedSales = "For: " + todaysDate.format(formattedDate) + ", Number of sales: " + numberOfOrders + ", " +
                "Totaling: $" + salesTotal;
        System.out.println(formattedSales);
        return salesToday;
    }
//try catch blocks
    private OrderReturnedToOwner ownersGetOrderDtoConverter(Orders order) {
        OrderReturnedToOwner orderReturnedToOwner = new OrderReturnedToOwner();
//        set the dto
        if (order.getCustomerId() != null) {
            orderReturnedToOwner.setName(customerRepository.getReferenceById(order.getCustomerId()).getName());
            orderReturnedToOwner.setEmail(customerRepository.getReferenceById(order.getCustomerId()).getEmail());
            orderReturnedToOwner.setPhone(customerRepository.getReferenceById(order.getCustomerId()).getPhoneNumber());
        }
        orderReturnedToOwner.setOrderUid(order.getOrderUid());

//        set the get order items dto
        List<OrderItemReturnedToOwner> orderItemReturnedToOwners = new ArrayList<>();
        List<OrderItem> orderItems = order.getOrderItems();

        orderItems.forEach(orderItem -> orderItemReturnedToOwners.add(ownersOrderItemDtoConvertor(orderItem)));

        orderReturnedToOwner.setOrderItems(orderItemReturnedToOwners);
        orderReturnedToOwner.setOrderTotal(order.getOrderTotal());
        orderReturnedToOwner.setCreated(order.getCreated());
        orderReturnedToOwner.setReady(order.getReady());
        orderReturnedToOwner.setClosed(order.getClosed());
        return orderReturnedToOwner;
    }

    private OrderItemReturnedToOwner ownersOrderItemDtoConvertor(OrderItem orderItem){
        OrderItemReturnedToOwner orderItemReturnedToOwner = new OrderItemReturnedToOwner();

        orderItemReturnedToOwner.setOrderItemId(orderItem.getOrderItemId());
        orderItemReturnedToOwner.setItemName(orderItem.getItem().getItemName());
        orderItemReturnedToOwner.setQuantity(orderItem.getQuantity());
        orderItemReturnedToOwner.setTotal(orderItem.getTotal());
        orderItemReturnedToOwner.setSize(orderItem.getSize());
//        set size
        return orderItemReturnedToOwner;
    }

    private BigDecimal calcPrice(Integer quantity, String size, BigDecimal unitPrice){
        BigDecimal sizeSurcharge = BigDecimal.ZERO;
        BigDecimal adjPrice;
        if (size.equalsIgnoreCase("M")) {
            sizeSurcharge = BigDecimal.valueOf(0.5);
        } else if (size.equalsIgnoreCase( "L")) {
            sizeSurcharge = BigDecimal.valueOf(1.0);
        }
        adjPrice = BigDecimal.valueOf(quantity).multiply(unitPrice.add(sizeSurcharge));
        return adjPrice;
    }
}
