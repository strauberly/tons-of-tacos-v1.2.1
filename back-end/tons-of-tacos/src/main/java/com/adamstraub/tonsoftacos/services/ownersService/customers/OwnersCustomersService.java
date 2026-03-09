package com.adamstraub.tonsoftacos.services.ownersService.customers;

import com.adamstraub.tonsoftacos.dao.MenuItemRepository;
import com.adamstraub.tonsoftacos.dao.OrderItemRepository;
import com.adamstraub.tonsoftacos.dao.CustomerRepository;
import com.adamstraub.tonsoftacos.dao.OrdersRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import com.adamstraub.tonsoftacos.entities.Customer;
import com.adamstraub.tonsoftacos.services.customerValidationService.CustomerValidationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.*;
@Data
@Service
@Slf4j
public class OwnersCustomersService implements OwnersCustomersServiceInterface {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private CustomerValidationService validationService;



    @Override
    public ResponseEntity<ResponseMessage> updateCustomerName(String customerUid, String newCustomerName) {

        System.out.println("update customer service");
        ResponseMessage message = new ResponseMessage();
        Customer customer;
        try {
            customer = customerRepository.findByCustomerUid(customerUid);
        } catch (Exception e) {
            throw new EntityNotFoundException("No customer with supplied uid(" + customerUid + ")found.");
        }
        String oldName = customer.getName();
        if (Objects.equals(oldName, newCustomerName)) {
            throw new IllegalArgumentException("New customer name can not be same as previous name.");
        } else if (!validationService.validateCustomerName(newCustomerName)) {
            throw new IllegalStateException("Incorrect formatting for customer. Consult documentation.");
        }else{
            customer.setName(newCustomerName);
            customerRepository.save(customer);
            message.setMessage("Previous customer name: " + oldName + ", updated to: " + customer.getName() + ".");
            return ResponseEntity.ok(message);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> updateCustomerEmail(String customerUid, String newCustomerEmail) {
        System.out.println("update customer service");
        Customer customer;
        ResponseMessage message = new ResponseMessage();
        try {
            customer = customerRepository.findByCustomerUid(customerUid);
        } catch (Exception e) {
            throw new EntityNotFoundException("No customer with supplied uid(" + customerUid + ")found.");
        }
        String oldEmail = customer.getEmail();
        if (Objects.equals(oldEmail, newCustomerEmail)) {
            throw new IllegalArgumentException("New customer email can not be same as previous.");
        }
        if (!validationService.validateCustomerEmail(newCustomerEmail)) {
            throw new IllegalArgumentException("Email does not match formatting requirements, please consult documentation.");
        }
        customer.setEmail(newCustomerEmail);
        customerRepository.save(customer);
        message.setMessage("Previous customer email: " + oldEmail + ", updated to: " + customer.getEmail() + ".");
        return ResponseEntity.ok(message);
    }
//
    @Override
    public ResponseEntity<ResponseMessage> updateCustomerPhone(String customerUid, String newCustomerPhone) {
        System.out.println("update customer service");
        Customer customer;
        ResponseMessage message = new ResponseMessage();

        try {
            customer = customerRepository.findByCustomerUid(customerUid);
        } catch (Exception e) {
            throw new EntityNotFoundException("No customer with supplied uid(" + customerUid + ")found.");
        }
        String oldCustomerPhone = customer.getPhoneNumber();
        if (Objects.equals(oldCustomerPhone, newCustomerPhone)) {
            throw new IllegalArgumentException("New customer phone can not be same as previous.");
        }
        if (!validationService.validateCustomerPhone(newCustomerPhone)){
            throw new NumberFormatException("New phone number invalid. Please check formatting and ensure new number is not the same as old number.");
        }else {
            customer.setPhoneNumber(newCustomerPhone);
            customerRepository.save(customer);
            message.setMessage("Previous customer phone number: " + oldCustomerPhone + ", updated to: " + customer.getPhoneNumber() + ".");
            return ResponseEntity.ok(message);
        }
    }
}
