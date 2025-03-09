package com.example.vouchermanager.Model.DTO;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VoucherUsageDTO {
    private int usageId;
    private int orderId;
    private String voucherCode;
    private LocalDateTime userDate;

    @Override
    public String toString() {
        return "VoucherUsageDTO{" +
                "usageId=" + usageId +
                ", orderId=" + orderId +
                ", voucherCode='" + voucherCode + '\'' +
                ", useDate=" + userDate +
                '}';
    }

}
