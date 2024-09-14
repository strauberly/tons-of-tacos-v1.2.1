package com.adamstraub.tonsoftacos.dto.customerDto.ordersDto;

//import com.adamstraub.tonsoftacos.dto.customerDto.orderItemsDto.OrderItems;
import com.adamstraub.tonsoftacos.dto.customerDto.orderItemsDto.OrderItemDTO;
import com.adamstraub.tonsoftacos.entities.Customer;
import com.adamstraub.tonsoftacos.entities.OrderItem;
import com.adamstraub.tonsoftacos.entities.Orders;
import lombok.Data;

import java.util.List;

@Data
public class SubmittedOrder {
    private Customer customer;
     private List<OrderItemDTO> order;
//private Orders order;
}
