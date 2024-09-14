package com.adamstraub.tonsoftacos.dto.customerDto.orderItemsDto;

import lombok.Data;

import java.util.List;

@Data
public class OrderItems {
    private List<OrderItemDTO> submittedOrderItemDTOS;
}
