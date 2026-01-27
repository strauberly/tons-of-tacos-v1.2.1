package com.adamstraub.tonsoftacos.controllers.ordersControllers;

import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.OrderReturnedToCustomer;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.SubmittedOrder;
import com.adamstraub.tonsoftacos.services.ordersService.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrdersController implements OrdersControllerInterface {
    @Autowired
    private OrdersService ordersService;


    @Override
    public  OrderReturnedToCustomer createOrder(@RequestBody SubmittedOrder order) {
        System.out.println("controller");
        return ordersService.createOrder(order);
    }
}
