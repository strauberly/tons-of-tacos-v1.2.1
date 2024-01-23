package com.adamstraub.tonsoftacos.services.ordersServices;
import com.adamstraub.tonsoftacos.dto.businessDto.NewOrder;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.OrderReturnedToCustomer;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrdersServiceInterface {

    OrderReturnedToCustomer createOrder(@RequestBody NewOrder order) throws Exception;
}