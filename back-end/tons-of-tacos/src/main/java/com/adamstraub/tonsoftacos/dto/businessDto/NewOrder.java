package com.adamstraub.tonsoftacos.dto.businessDto;

import com.adamstraub.tonsoftacos.entities.Customer;
import com.adamstraub.tonsoftacos.entities.Orders;
import lombok.Data;

@Data
public class NewOrder {
    private Customer customer;
    private Orders order;

}
