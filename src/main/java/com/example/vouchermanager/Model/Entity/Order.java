package com.example.vouchermanager.Model.Entity;

import com.example.vouchermanager.Model.Enum.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "UserID")
    private User userID;

    @ColumnDefault("current_timestamp()")
    @Column(name = "OrderDate")
    private Instant orderDate;

    @Column(name = "TotalAmount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "FinalAmount", nullable = false, precision = 10, scale = 2)
    private BigDecimal finalAmount;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'PENDING'")
    @Lob
    @Column(name = "OrderStatus", nullable = false)
    private OrderStatus orderStatus;

    @Override
    public String toString() {
        return "Order{" +
                "Order ID:" + id + '\''+
                "User ID:" + userID + '\''+
                "Order date:" + orderDate + '\''+
                "Total Amount:" + totalAmount + '\''+
                "Final Amount:" + finalAmount + '\''+
                "Order status:" + orderStatus + '\''+
                '}';

    }
}