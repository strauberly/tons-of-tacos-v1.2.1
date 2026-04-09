package com.adamstraub.tonsoftacos.services.ownersService.customers;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessageDTO;
import org.springframework.http.ResponseEntity;

public interface IOwnersCustomersService {
 ResponseEntity<ResponseMessageDTO> updateCustomerName(String customerUid, String newCustomerName);
 ResponseEntity<ResponseMessageDTO>updateCustomerEmail(String customerUid, String newCustomerEmail);
 ResponseEntity<ResponseMessageDTO> updateCustomerPhone(String customerUid, String newCustomerPhone);

}
