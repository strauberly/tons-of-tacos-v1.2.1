package com.adamstraub.tonsoftacos.dto.customerDto.ordersDto;

import com.adamstraub.tonsoftacos.dto.customerDto.orderItemDto.OrderItemReturnedToCustomer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderReturnedToCustomer {
    private String customerName;
    private String customerEmail;
    private  String customerPhone;
    private String orderUid;
    private List<OrderItemReturnedToCustomer> orderItems;
    private BigDecimal orderTotal;

}
