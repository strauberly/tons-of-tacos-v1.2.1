package com.adamstraub.tonsoftacos.services;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class UtilityService {
    public BigDecimal calcPrice(Integer quantity, String size, BigDecimal unitPrice){
        BigDecimal sizeSurcharge = BigDecimal.ZERO;
        BigDecimal adjPrice;
        if (size.equalsIgnoreCase("M")) {
            sizeSurcharge = BigDecimal.valueOf(0.5);
        } else if (size.equalsIgnoreCase( "L")) {
            sizeSurcharge = BigDecimal.valueOf(1.0);
        }
        adjPrice = BigDecimal.valueOf(quantity).multiply(unitPrice.add(sizeSurcharge));
        return adjPrice;
    }
}
