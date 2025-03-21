package com.example.vouchermanager.Model.DTO;

import java.math.BigDecimal;

public class VoucherUsageResultDTO {
    private boolean success;
    private BigDecimal discountValue;
    private String message;

    public VoucherUsageResultDTO(boolean success, BigDecimal discountValue, String message) {
        this.success = success;
        this.discountValue = discountValue;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public BigDecimal getDiscountValue() { return discountValue; }
    public String getMessage() { return message; }
}