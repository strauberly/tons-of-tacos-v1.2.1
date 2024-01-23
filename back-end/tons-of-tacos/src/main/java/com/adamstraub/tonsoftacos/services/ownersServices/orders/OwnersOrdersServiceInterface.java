package com.adamstraub.tonsoftacos.services.ownersServices.orders;
import com.adamstraub.tonsoftacos.dto.businessDto.DailySales;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwner;

import java.util.List;

public interface OwnersOrdersServiceInterface {

    List<OrderReturnedToOwner> getAllOrders();


    OrderReturnedToOwner getOrderByUid(String orderUid);


    List<OrderReturnedToOwner> getOpenOrderByCustomer(String customer);

    OrderReturnedToOwner orderReady(String orderUid);


    OrderReturnedToOwner closeOrder(String orderUid);

    String deleteOrder(String orderUid);

    String addToOrder(String orderUid, Integer menuItemId, Integer quantity);

    String updateOrderItemQuantity(String orderUid, Integer orderItemId, Integer newQuantity);

    DailySales todaysSales();

}
