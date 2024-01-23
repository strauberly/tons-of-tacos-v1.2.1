package com.adamstraub.tonsoftacos.dto.customerDto.ordersDto;

import com.adamstraub.tonsoftacos.dto.customerDto.orderItemsDto.OrderItemReturnedToCustomer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderReturnedToCustomer {
    private String customerName;
    private String orderUid;
    private List<OrderItemReturnedToCustomer> orderItems;
    private BigDecimal orderTotal;

}
