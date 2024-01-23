package com.adamstraub.tonsoftacos.dto.businessDto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderReturnedToOwner {
    private String orderUid;
    private String name = null;
    private String email = null;
    private String phone = null;
    private List<OrderItemReturnedToOwner> orderItems;
    private BigDecimal orderTotal;
    private Timestamp created;
    private String ready;
    private String closed;
}
