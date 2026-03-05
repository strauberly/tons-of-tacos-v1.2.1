package com.adamstraub.tonsoftacos.services.ownersService.customers;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import org.springframework.http.ResponseEntity;

public interface OwnersCustomersServiceInterface {
 ResponseEntity<ResponseMessage> updateCustomerName(String customerUid, String newCustomerName);
 ResponseEntity<ResponseMessage>updateCustomerEmail(String customerUid, String newCustomerEmail);
 ResponseEntity<ResponseMessage> updateCustomerPhone(String customerUid, String newCustomerPhone);

}
