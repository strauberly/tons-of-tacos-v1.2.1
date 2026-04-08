package com.adamstraub.tonsoftacos.repository;
import com.adamstraub.tonsoftacos.entities.OrderItem;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    OrderItem getByOrderItemId(@RequestParam("orderItemId")Integer orderItemId) throws EntityNotFoundException;
}
