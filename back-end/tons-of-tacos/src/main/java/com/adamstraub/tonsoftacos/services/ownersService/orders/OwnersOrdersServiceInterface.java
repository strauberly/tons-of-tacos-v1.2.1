package com.adamstraub.tonsoftacos.services.ownersService.orders;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwnerDTO;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessageDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OwnersOrdersServiceInterface {

    ResponseEntity<List<OrderReturnedToOwnerDTO>> getAllOrders();

    ResponseEntity<OrderReturnedToOwnerDTO> getOrderByUid(String orderUid);

    ResponseEntity<List<OrderReturnedToOwnerDTO>> getOrdersByPhoneNumber(String phoneNumber);

    ResponseEntity<OrderReturnedToOwnerDTO> orderReady(String orderUid);

    ResponseEntity<OrderReturnedToOwnerDTO> closeOrder(String orderUid);

    ResponseEntity<ResponseMessageDTO> deleteOrder(String orderUid);

    ResponseEntity<ResponseMessageDTO> addToOrder(String orderUid, Integer menuItemId, Integer quantity, String itemSize);

    ResponseEntity<ResponseMessageDTO> removeFromOrder(Integer orderItemId);

    ResponseEntity<ResponseMessageDTO> updateOrderItemQuantity(String orderUid, Integer orderItemId, Integer newQuantity, String newSize);

//    ResponseEntity<DailySales> todaysSales();



}
