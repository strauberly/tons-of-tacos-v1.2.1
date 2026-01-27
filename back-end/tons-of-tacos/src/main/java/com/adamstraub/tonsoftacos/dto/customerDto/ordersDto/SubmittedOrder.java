package com.adamstraub.tonsoftacos.dto.customerDto.ordersDto;

import com.adamstraub.tonsoftacos.dto.customerDto.orderItemDto.OrderItemDTO;
import com.adamstraub.tonsoftacos.entities.Customer;
import lombok.Data;

import java.util.List;

@Data
public class SubmittedOrder {
    private Customer customer;
     private List<OrderItemDTO> order;
}
