package com.adamstraub.tonsoftacos.services.ownersService.customers;
import com.adamstraub.tonsoftacos.dto.businessDto.CustomerReturnedToOwner;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface OwnersCustomersServiceInterface {

ResponseMessage updateCustomer(String customerUid, String name, String phone, String email);
}
