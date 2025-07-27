package com.adamstraub.tonsoftacos.services.ownersServices.orders;
import com.adamstraub.tonsoftacos.dto.businessDto.DailySales;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwner;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;

import java.util.List;

public interface OwnersOrdersServiceInterface {

    List<OrderReturnedToOwner> getAllOrders();


    OrderReturnedToOwner getOrderByUid(String orderUid);


//    List<OrderReturnedToOwner> getOpenOrderByCustomer(String customer);

    List<OrderReturnedToOwner> getOrdersByCustomer(String customer);

    OrderReturnedToOwner orderReady(String orderUid);


    OrderReturnedToOwner closeOrder(String orderUid);

    ResponseMessage deleteOrder(String orderUid);

    ResponseMessage addToOrder(String orderUid, Integer menuItemId, Integer quantity, String itemSize);

    ResponseMessage removeFromOrder( Integer orderItemId);

    ResponseMessage updateOrderItemQuantity(String orderUid, Integer orderItemId, Integer newQuantity, String newSize);

    DailySales todaysSales();

//    List<OrderReturnedToOwner> getOrdersByPhone(String phone);

    List<OrderReturnedToOwner> getOrdersByPhoneNumber(String phoneNumber);

//    List<OrderReturnedToOwner> getOrdersByPhone(String phone);
}
