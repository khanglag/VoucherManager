package com.example.vouchermanager.Model.DTO;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class VoucherPerformanceDTO {
    private String voucherCode;
    private int totalIssued;
    private int totalUsed;
    private int totalRemaining;
    private BigDecimal efficiency;
    private Integer userId;
    private String userName;

    @Override
    public String toString() {
        return "VoucherPerformanceDTO{" +
                "voucherCode='" + voucherCode + '\'' +
                ", totalIssued=" + totalIssued +
                ", totalUsed=" + totalUsed +
                ", totalRemaining=" + totalRemaining +
                ", efficiency=" + efficiency +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
