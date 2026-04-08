package com.adamstraub.tonsoftacos.repository;

import com.adamstraub.tonsoftacos.entities.Customer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByCustomerUid(@RequestParam("customer_uid") String customerUid) throws EntityNotFoundException;

    List<Customer> findByNameContaining(String name) throws  EntityNotFoundException;

    List<Customer> findByPhoneNumber(@RequestParam("phone_number") String phone) throws EntityNotFoundException;

}
