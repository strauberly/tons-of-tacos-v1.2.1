package com.adamstraub.tonsoftacos.services.utilityService.salesService;

import com.adamstraub.tonsoftacos.dto.businessDto.DailySalesDTO;
import com.adamstraub.tonsoftacos.entities.Orders;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface ISalesService {
    BigDecimal calcItemPriceWithSize(Integer quantity, String size, BigDecimal unitPrice);
    ResponseEntity<DailySalesDTO> salesToday();
    List<Orders> findClosedOrders();
    String ordersStats();
    String largestSale();
}
