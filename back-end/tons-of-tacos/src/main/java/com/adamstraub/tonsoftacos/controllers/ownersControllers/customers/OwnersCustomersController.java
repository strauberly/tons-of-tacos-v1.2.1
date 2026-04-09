package com.adamstraub.tonsoftacos.controllers.ownersControllers.customers;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessageDTO;
import com.adamstraub.tonsoftacos.services.ownersService.customers.IOwnersCustomersService;
import com.adamstraub.tonsoftacos.services.ownersService.customers.OwnersCustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OwnersCustomersController implements OwnersCustomersControllerInterface {

    @Autowired
    private IOwnersCustomersService ownersCustomersService;


    @Override
    public ResponseEntity<ResponseMessageDTO> updateCustomerName(String customerUid, String newCustomerName) {
        return ownersCustomersService.updateCustomerName(customerUid, newCustomerName);
    }

    @Override
    public ResponseEntity<ResponseMessageDTO> updateCustomerEmail(String customerUid, String newCustomerEmail) {
        System.out.println("Owners Customers Controller");
        return ownersCustomersService.updateCustomerEmail(customerUid, newCustomerEmail);
    }

    @Override
    public ResponseEntity<ResponseMessageDTO> updateCustomerPhone(String customerUid, String newCustomerPhone) {
        System.out.println("Owners Customers Controller");
        return ownersCustomersService.updateCustomerPhone(customerUid, newCustomerPhone);
    }
}
