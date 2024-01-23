package com.adamstraub.tonsoftacos.controllers.ordersControllers;

import com.adamstraub.tonsoftacos.dto.businessDto.NewOrder;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.OrderReturnedToCustomer;
import com.adamstraub.tonsoftacos.services.ordersServices.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrdersController implements OrdersControllerInterface {
    @Autowired
    private OrdersService ordersService;

    @Override
    public OrderReturnedToCustomer createOrder(@RequestBody NewOrder order) {
            System.out.println("controller");
            return ordersService.createOrder(order);
    }
}
