package com.adamstraub.tonsoftacos.dao;

import com.adamstraub.tonsoftacos.entities.Customer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByName(@RequestParam("name") String customer);

    @NotNull Customer getById(@RequestParam("id") @NotNull Integer customer);

    Customer findByCustomerUid(@RequestParam("customer_uid") String customerUid);

}
