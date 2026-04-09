package com.adamstraub.tonsoftacos.controllers.ordersControllers;

import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.OrderReturnedToCustomerDTO;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.SubmittedOrderDTO;
import com.adamstraub.tonsoftacos.services.ordersService.IOrdersService;
import com.adamstraub.tonsoftacos.services.ordersService.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrdersController implements OrdersControllerInterface {
    @Autowired
    private IOrdersService ordersService;


    @Override
    public ResponseEntity<OrderReturnedToCustomerDTO> createOrder(@RequestBody SubmittedOrderDTO order) throws Exception {
        System.out.println("controller");
        return ordersService.createOrder(order);
    }
}
