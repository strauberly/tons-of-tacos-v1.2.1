package com.adamstraub.tonsoftacos.dto.customerDto.orderItemDto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private String menuId;
    private Integer quantity;
    private String size;

}


