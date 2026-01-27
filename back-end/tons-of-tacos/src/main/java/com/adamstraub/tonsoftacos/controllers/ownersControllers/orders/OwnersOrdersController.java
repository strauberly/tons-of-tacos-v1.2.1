package com.adamstraub.tonsoftacos.controllers.ownersControllers.orders;

import com.adamstraub.tonsoftacos.dto.businessDto.DailySales;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwner;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import com.adamstraub.tonsoftacos.services.ownersService.orders.OwnersOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class    OwnersOrdersController implements OwnersOrdersControllerInterface {

    @Autowired
    private OwnersOrdersService ownersOrdersService;

    @Override
    public List<OrderReturnedToOwner> getAllOrders() {
        System.out.println("Owners Orders Controller");
        return ownersOrdersService.getAllOrders();
    }
    @Override
    public List<OrderReturnedToOwner> getOrdersByPhoneNumber(String phone) {
        return ownersOrdersService.getOrdersByPhoneNumber(phone);
    }



    @Override
    public OrderReturnedToOwner getOrderByUid(@PathVariable String orderUid) {
        System.out.println("Owners Orders Controller");
        return ownersOrdersService.getOrderByUid(orderUid);
    }

    @Override
    public List<OrderReturnedToOwner> getOrdersByCustomer(String customer) {
        System.out.println("Owners Orders Controller");
//        return ownersOrdersService.getOpenOrderByCustomer(customer);
        return ownersOrdersService.getOrdersByCustomer(customer);
    }


    @Override
    public OrderReturnedToOwner orderReady(String orderUid) {
        System.out.println("Owners Orders Controller");
        return ownersOrdersService.orderReady(orderUid);
    }

    @Override
    public OrderReturnedToOwner closeOrder(String orderUid) {
            System.out.println("Owners Orders Controller");
       return ownersOrdersService.closeOrder(orderUid);
    }


    @Override
    public ResponseMessage deleteOrder(String orderUid) {
        System.out.println("Owners Orders Controller");
        return ownersOrdersService.deleteOrder(orderUid);
    }

    @Override
    public ResponseMessage addToOrder(String orderUid, Integer menuItemId, Integer quantity, String itemSize) {
        System.out.println("Owners Orders Controller");

        return ownersOrdersService.addToOrder(orderUid, menuItemId, quantity, itemSize);
    }

    @Override
    public ResponseMessage removeFromOrder(Integer orderItemId) {
        System.out.println("Owners Orders Controller");

        return ownersOrdersService.removeFromOrder(orderItemId);
    }

    @Override
    public ResponseMessage updateOrderItemQuantity(String orderUid, Integer orderItemId, Integer newQuantity, String newSize) {
            System.out.println("Owners Orders Controller");
        return ownersOrdersService.updateOrderItemQuantity(orderUid, orderItemId, newQuantity, newSize);
    }

    @Override
    public DailySales todaysSales() {
        System.out.println("Owners Orders Controller");
        return ownersOrdersService.todaysSales();
    }
}
