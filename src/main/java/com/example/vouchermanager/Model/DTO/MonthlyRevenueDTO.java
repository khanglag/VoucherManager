package com.example.vouchermanager.Model.DTO;

import java.math.BigDecimal;

public class MonthlyRevenueDTO {
    private int month;
    private BigDecimal totalAmount;

    public MonthlyRevenueDTO(int month, BigDecimal totalAmount) {
        this.month = month;
        this.totalAmount = totalAmount;
    }

    // Getter và Setter
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
