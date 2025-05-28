package com.adamstraub.tonsoftacos.dto.customerDto.orderItemsDto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private String menuId;
    private Integer quantity;
//    private Character size;
    private String size;

}


