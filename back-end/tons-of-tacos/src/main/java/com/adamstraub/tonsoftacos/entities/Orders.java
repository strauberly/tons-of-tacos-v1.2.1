package com.adamstraub.tonsoftacos.entities;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Builder
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "order_pk")
    private Integer orderId;

    @Column(name = "customer_fk")
    private Integer customerId;

    @Column (name = "order_total")
    private BigDecimal orderTotal;

    @Column (name = "order_uid")
    private String orderUid;

    @Column (name = "created")
    @CreationTimestamp
    private Timestamp created;

    @Column (name = "ready")
    private String ready = "no";

    @Column (name = "closed")
    private String closed = "no";

    @Column (name = "customer_uid")
    private String customerUid;


@OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();


    @Override
    public String toString() {
        return "Orders{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", orderTotal=" + orderTotal +
                ", orderUid='" + orderUid + '\'' +
                ", created=" + created +
                ", ready='" + ready + '\'' +
                ", closed='" + closed + '\'' +
                ", customerUid='" + customerUid + '\'' +
                '}';
    }

//    public void setCustomerUid(String s) {
//    }
}
