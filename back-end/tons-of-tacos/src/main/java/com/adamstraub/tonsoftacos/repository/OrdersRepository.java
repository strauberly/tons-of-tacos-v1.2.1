package com.adamstraub.tonsoftacos.repository;

import com.adamstraub.tonsoftacos.entities.Orders;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

Orders findByOrderUid (@RequestParam ("order_uid") String orderUid) throws EntityNotFoundException;
Orders findTopByOrderByOrderTotalDesc() throws EntityNotFoundException;
List<Orders> findByCustomerUid(@RequestParam("customer_uid") String customerUid) throws EntityNotFoundException;
@NotNull Orders getById(@RequestParam("order_pk") @NotNull Integer orderId) throws EntityNotFoundException;
}

