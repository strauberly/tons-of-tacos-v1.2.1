package com.adamstraub.tonsoftacos.dao;
import com.adamstraub.tonsoftacos.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> order(@RequestParam("order") String order);

    OrderItem getByOrderItemId(@RequestParam("orderItemId")Integer orderItemId);
}
