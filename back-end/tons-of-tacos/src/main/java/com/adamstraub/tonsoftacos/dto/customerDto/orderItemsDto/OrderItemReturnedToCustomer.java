package com.adamstraub.tonsoftacos.dto.customerDto.orderItemsDto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemReturnedToCustomer {
     private String itemName;
     private BigDecimal unitPrice;
     private Integer quantity;
     private BigDecimal total;

}
