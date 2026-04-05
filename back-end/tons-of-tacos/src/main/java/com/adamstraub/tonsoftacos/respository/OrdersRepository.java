package com.adamstraub.tonsoftacos.respository;

import com.adamstraub.tonsoftacos.entities.Orders;
import jakarta.persistence.EntityNotFoundException;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {

Orders findByOrderUid (@RequestParam ("order_uid") String orderUid);
Orders findTopByOrderByOrderTotalDesc();
List<Orders> findByCustomerUid(@RequestParam("customer_uid") String customerUid) throws EntityNotFoundException;
@NotNull Orders getById(@RequestParam("order_pk") @NotNull Integer orderId);
}

