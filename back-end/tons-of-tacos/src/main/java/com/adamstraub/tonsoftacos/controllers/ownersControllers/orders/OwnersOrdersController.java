package com.adamstraub.tonsoftacos.controllers.ownersControllers.orders;

import com.adamstraub.tonsoftacos.dto.businessDto.DailySalesDTO;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwnerDTO;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessageDTO;
import com.adamstraub.tonsoftacos.services.ownersService.orders.OwnersOrdersService;
import com.adamstraub.tonsoftacos.services.utilityService.salesService.SalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
public class    OwnersOrdersController implements OwnersOrdersControllerInterface {

    @Autowired
    private OwnersOrdersService ownersOrdersService;
    @Autowired
    private SalesService salesService;

    @Override
    public ResponseEntity<List<OrderReturnedToOwnerDTO>> getAllOrders() {
        return ownersOrdersService.getAllOrders();
    }

    @Override
    public ResponseEntity<List<OrderReturnedToOwnerDTO>> getOrdersByPhoneNumber(String phone) {
        return ownersOrdersService.getOrdersByPhoneNumber(phone);
    }

    @Override
    public ResponseEntity<OrderReturnedToOwnerDTO> getOrderByUid(@PathVariable String orderUid) {
        return ownersOrdersService.getOrderByUid(orderUid);
    }
    @Override
    public ResponseEntity<OrderReturnedToOwnerDTO> orderReady(String orderUid) {
        System.out.println("Owners Orders Controller");
        return ownersOrdersService.orderReady(orderUid);
    }

    @Override
    public ResponseEntity<OrderReturnedToOwnerDTO> closeOrder(String orderUid) {
            System.out.println("Owners Orders Controller");
       return ownersOrdersService.closeOrder(orderUid);
    }


    @Override
    public ResponseEntity<ResponseMessageDTO> deleteOrder(String orderUid) {
        System.out.println("Owners Orders Controller");
        return ownersOrdersService.deleteOrder(orderUid);
    }

    @Override
    public ResponseEntity<ResponseMessageDTO> addToOrder(String orderUid, Integer menuItemId, Integer quantity, String itemSize) {
        System.out.println("Owners Orders Controller");

        return ownersOrdersService.addToOrder(orderUid, menuItemId, quantity, itemSize);
    }

    @Override
    public ResponseEntity<ResponseMessageDTO> removeFromOrder(Integer orderItemId) {
        System.out.println("Owners Orders Controller");

        return ownersOrdersService.removeFromOrder(orderItemId);
    }

    @Override
    public ResponseEntity<ResponseMessageDTO> updateOrderItemQuantity(String orderUid, Integer orderItemId, Integer newQuantity, String newSize) {
            System.out.println("Owners Orders Controller");
        return ownersOrdersService.updateOrderItemQuantity(orderUid, orderItemId, newQuantity, newSize);
    }

    @Override
    public ResponseEntity<DailySalesDTO> todaysSales() {
        System.out.println("Owners Orders Controller");
        return salesService.salesToday();
    }
}
