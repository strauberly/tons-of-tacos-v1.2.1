package com.adamstraub.tonsoftacos.services.ownersService.orders;
import com.adamstraub.tonsoftacos.dto.businessDto.DailySales;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwner;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OwnersOrdersServiceInterface {

    ResponseEntity<List<OrderReturnedToOwner>> getAllOrders();

    ResponseEntity<OrderReturnedToOwner> getOrderByUid(String orderUid);

    ResponseEntity<List<OrderReturnedToOwner>> getOrdersByPhoneNumber(String phoneNumber);

    ResponseEntity<OrderReturnedToOwner> orderReady(String orderUid);

    ResponseEntity<OrderReturnedToOwner> closeOrder(String orderUid);

    ResponseEntity<ResponseMessage> deleteOrder(String orderUid);

    ResponseEntity<ResponseMessage> addToOrder(String orderUid, Integer menuItemId, Integer quantity, String itemSize);

    ResponseEntity<ResponseMessage> removeFromOrder( Integer orderItemId);

    ResponseEntity<ResponseMessage> updateOrderItemQuantity(String orderUid, Integer orderItemId, Integer newQuantity, String newSize);

    ResponseEntity<DailySales> todaysSales();



}
