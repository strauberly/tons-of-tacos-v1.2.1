package com.adamstraub.tonsoftacos.controllers.ownersControllers.customers;
import com.adamstraub.tonsoftacos.dto.businessDto.CustomerReturnedToOwner;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import com.adamstraub.tonsoftacos.services.ownersService.customers.OwnersCustomersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Slf4j
@RestController
public class OwnersCustomersController implements OwnersCustomersControllerInterface {


    @Autowired
    private OwnersCustomersService ownersCustomersService;


    @Override
    public ResponseEntity<ResponseMessage> updateCustomerName(String customerUid, String newCustomerName) {
        return ownersCustomersService.updateCustomerName(customerUid, newCustomerName);
    }

//    @Override
//    public ResponseEntity<ResponseMessage> updateCustomerEmail(String customerUid, String newCustomerEmail) {
//        System.out.println("Owners Customers Controller");
//        return ownersCustomersService.updateCustomerEmail(customerUid, newCustomerEmail);
//    }

//    @Override
//    public String updateCustomerPhone(String customerUid, String newCustomerPhone) {
//        System.out.println("Owners Customers Controller");
//        return ownersCustomersService.updateCustomerPhone(customerUid, newCustomerPhone);
//    }

//    @Override
//    public ResponseMessage updateCustomer(String customerUid, String name, String phone, String email) {
//        System.out.println("Owners Customers Controller");
//        return ownersCustomersService.updateCustomer(customerUid, name, phone, email);
//    }

//    @Override
//    public String deleteCustomer(String customerUid) {
//        System.out.println("Owners Customers Controller");
//        return ownersCustomersService.deleteCustomer(customerUid);
//    }

}
