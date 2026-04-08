package com.adamstraub.tonsoftacos.dto.customerDto.ordersDto;

import com.adamstraub.tonsoftacos.dto.customerDto.orderItemDto.OrderItemReturnedToCustomerDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderReturnedToCustomerDTO {
    private String customerName;
    private String customerEmail;
    private  String customerPhone;
    private String orderUid;
    private List<OrderItemReturnedToCustomerDTO> orderItems;
    private BigDecimal orderTotal;

}
