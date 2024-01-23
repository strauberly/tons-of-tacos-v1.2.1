package com.adamstraub.tonsoftacos.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Builder
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "order_item_pk")
    private Integer orderItemId;

    @ManyToOne()
    @JoinColumn(name = "item_fk")
    private MenuItem item;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "total")
    private BigDecimal total;

    @ManyToOne()
    @JoinColumn(name = "order_fk")
    private Orders order;

    @JsonBackReference
    public Orders getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "orderItemId=" + orderItemId +
                ", id=" + item +
                ", order='" + order + '\'' +
                ", quantity=" + quantity +
                ", total=" + total +
                '}';
    }
}