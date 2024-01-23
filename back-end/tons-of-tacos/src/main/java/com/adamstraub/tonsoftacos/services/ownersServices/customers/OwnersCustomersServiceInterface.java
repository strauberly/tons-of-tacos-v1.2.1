package com.adamstraub.tonsoftacos.services.ownersServices.customers;
import com.adamstraub.tonsoftacos.dto.businessDto.CustomerReturnedToOwner;

import java.util.List;


public interface OwnersCustomersServiceInterface {

List<CustomerReturnedToOwner> getAllCustomers();

CustomerReturnedToOwner getCustomerByName(String name);

CustomerReturnedToOwner getCustomerByUid(String customerUid);

String updateCustomerName(String customerUid, String newCustomerName);

String updateCustomerEmail(String customerUid, String newCustomerEmail);

String updateCustomerPhone(String customerUid, String newCustomerPhone);

String deleteCustomer(String customerUid);
}
