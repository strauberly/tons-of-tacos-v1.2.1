package com.adamstraub.tonsoftacos.dto.businessDto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemReturnedToOwner {
    private Integer orderItemId;
    private String itemName;
    private Integer quantity;
    private char size;
    private BigDecimal total;


}
