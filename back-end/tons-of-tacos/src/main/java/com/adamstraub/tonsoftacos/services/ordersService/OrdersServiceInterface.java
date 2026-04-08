package com.adamstraub.tonsoftacos.services.ordersService;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.OrderReturnedToCustomerDTO;
import com.adamstraub.tonsoftacos.dto.customerDto.ordersDto.SubmittedOrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrdersServiceInterface {
    ResponseEntity<OrderReturnedToCustomerDTO> createOrder(@RequestBody SubmittedOrderDTO order) throws Exception;
}