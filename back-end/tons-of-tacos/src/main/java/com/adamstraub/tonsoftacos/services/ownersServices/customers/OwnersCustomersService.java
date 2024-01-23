package com.adamstraub.tonsoftacos.services.ownersServices.customers;

import com.adamstraub.tonsoftacos.dao.MenuItemRepository;
import com.adamstraub.tonsoftacos.dao.OrderItemRepository;
import com.adamstraub.tonsoftacos.dao.CustomerRepository;
import com.adamstraub.tonsoftacos.dao.OrdersRepository;
import com.adamstraub.tonsoftacos.dto.businessDto.CustomerReturnedToOwner;
import com.adamstraub.tonsoftacos.entities.Customer;
import com.adamstraub.tonsoftacos.entities.Orders;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.*;
@Data
@Service
public class OwnersCustomersService implements OwnersCustomersServiceInterface {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerReturnedToOwner> getAllCustomers() {
        System.out.println("service");
        List<CustomerReturnedToOwner> allCustomersDtos = new ArrayList<>();
        List<Customer> customers;
        try {
            customers = customerRepository.findAll();
            for (Customer customer : customers) {
                allCustomersDtos.add(ownersCustomerDtoConvertor(customer));
            }
        } catch (Exception e) {
            throw new EntityNotFoundException("No customers found at all. Please contact your application team right away.");
        }
        return  allCustomersDtos;
    }


    @Override
    public CustomerReturnedToOwner getCustomerByName(String name) {
        System.out.println("service");
            Customer customer = customerRepository.findByName(name);
            if (customer == null){
                throw new EntityNotFoundException("No customer found by that name. Please check your spelling" +
                    " and formatting.");
            }else {
                return ownersCustomerDtoConvertor(customer);
            }
    }

    @Override
    public CustomerReturnedToOwner getCustomerByUid(String customerUid) {
        System.out.println("service");
        Customer customer = customerRepository.findByCustomerUid(customerUid);

        if (customer == null) {
            throw new EntityNotFoundException("No customer found with that UID. Please verify and try  again.");
        } else{
            return ownersCustomerDtoConvertor(customer);
        }
    }

    @Override
    public String updateCustomerName(String customerUid, String newCustomerName) {
        System.out.println("service");
        Customer customer;

        customer = customerRepository.findByCustomerUid(customerUid);

        if (customer == null){
            throw new EntityNotFoundException("No customer with that id found.");
        }
            byte[] nameChars = newCustomerName.getBytes(StandardCharsets.UTF_8);
            int spaces = 0;
            boolean customerNameValid = true;
        String oldName = customer.getName();

        if (oldName.equals(newCustomerName)) {
            throw new IllegalArgumentException("New customer name can not be same as previous name.");
        }
        for (Byte namechar : nameChars) {
            if (Objects.equals(namechar, (byte) 32)) {
                spaces += 1;
            }
        }


//        possibly alter for just ^[a-zA-Z]$+ [a-zA-Z]+. currently accepting letters from any language.
        if (!newCustomerName.matches("^\\p{L}+[\\p{L}\\p{Pd}\\p{Zs}']*\\p{L}+$|^\\p{L}+$") ||
                !(spaces == 1)) {
            customerNameValid = false;
            System.out.println(customerNameValid);
            throw new IllegalArgumentException("Customer name incorrectly formatted. Please consult the documentation.");
        }
        if (customerNameValid) {
            customer.setName(newCustomerName);
            customerRepository.save(customer);
        }
        return "Previous customer name: " + oldName + ", updated to: " + customer.getName() + ".";
    }

    @Override

    public String updateCustomerEmail(String customerUid, String newCustomerEmail) {
        System.out.println("service");
        Customer customer;

            customer = customerRepository.findByCustomerUid(customerUid);
            if (customer == null){
                throw new EntityNotFoundException("No customer with that id found.");
            }

        String oldEmail = customer.getEmail();
        if (oldEmail.equals(newCustomerEmail)) {
            throw new IllegalArgumentException("New customer email can not be same as previous.");
        }
        if (!newCustomerEmail.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,}")) {
            throw new IllegalArgumentException("Email does not match formatting requirements, please consult the docs.");
        }
        customer.setEmail(newCustomerEmail);
        customerRepository.save(customer);
        return "Previous customer email: " + oldEmail + ", updated to: " + customer.getEmail() + ".";
    }

    @Override
    public String updateCustomerPhone(String customerUid, String newCustomerPhone) {
        System.out.println("service");

        boolean newCustomerPhoneNumberValid = false;
        Customer customer = customerRepository.findByCustomerUid(customerUid);

        if(customer == null){
            throw new EntityNotFoundException("No customer with that uid found. Please check formatting and verify customer exists.");
        }

        String oldCustomerPhone = customer.getPhoneNumber();

        if (newCustomerPhone.matches("[0-9.]*")
                && newCustomerPhone.charAt(3) == (char) 46
                && newCustomerPhone.charAt(7) == (char) 46
                && newCustomerPhone.length() == 12)
                {
            newCustomerPhoneNumberValid = true;
        }
        if (!newCustomerPhoneNumberValid || oldCustomerPhone.equals(newCustomerPhone)){
            throw new NumberFormatException("New phone number invalid. Please check formatting and ensure new number is not the same as old number.");
        }else {
            customer.setPhoneNumber(newCustomerPhone);
            customerRepository.save(customer);

            return "Previous customer phone number: " + oldCustomerPhone + ", updated to: " + customer.getPhoneNumber() + ".";
        }
    }

    @Override
    public String deleteCustomer(String customerUid) {
        System.out.println("service");
        Customer customer = customerRepository.findByCustomerUid(customerUid);
        if (customer == null){
            throw new EntityNotFoundException("No customer with that uid found. Please check formatting and verify customer exists.");
        }else {
        customerRepository.deleteById(customer.getCustomerId());
        System.out.println("Customer deleted");
        return "Customer " +  customer.getName() + "(" + customerUid + "), removed from application records.";
        }
    }


    private CustomerReturnedToOwner ownersCustomerDtoConvertor(Customer customer){
        CustomerReturnedToOwner ownersCustomerDto = new CustomerReturnedToOwner();
        ownersCustomerDto.setCustomerUid(customer.getCustomerUid());
        ownersCustomerDto.setName(customer.getName());
        ownersCustomerDto.setEmail(customer.getEmail());
        ownersCustomerDto.setPhone(customer.getPhoneNumber());

        List<Orders> orders = ordersRepository.findByCustomerId(customer.getCustomerId());
        List<String> orderIds = new ArrayList<>();
        orders.forEach(order -> orderIds.add(order.getOrderUid()));
        ownersCustomerDto.setOrderIds(orderIds);
        return ownersCustomerDto;
    }
}
