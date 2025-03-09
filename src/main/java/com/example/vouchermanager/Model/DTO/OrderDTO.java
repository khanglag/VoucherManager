package com.example.vouchermanager.Model.DTO;

import com.example.vouchermanager.Model.Enum.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDTO {
    private int orderId;
    private int userId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private BigDecimal finalAmount;
    private OrderStatus orderStatus;

    @Override
    public String toString() {
        return "Order{" +
                "Order ID:" + orderId + '\''+
                "User ID:" + userId + '\''+
                "Order date:" + orderDate + '\''+
                "Total Amount:" + totalAmount + '\''+
                "Final Amount:" + finalAmount + '\''+
                "Order status:" + orderStatus + '\''+
                '}';

    }
}
