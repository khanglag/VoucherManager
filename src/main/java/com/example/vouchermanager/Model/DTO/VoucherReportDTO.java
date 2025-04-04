package com.example.vouchermanager.Model.DTO;

import lombok.*;

import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class VoucherReportDTO {
    private String voucherCode;
    private String voucherType;
    private String discount;
    private int usageCount;
    private double totalDiscount;
    private double efficiency;
    @Override
    public String toString() {
        return "VoucherReportDTO{" +
                "voucherCode='" + voucherCode + '\'' +
                ", voucherType='" + voucherType + '\'' +
                ", discount='" + discount + '\'' +
                ", usageCount=" + usageCount +
                ", totalDiscount=" + totalDiscount +
                ", efficiency=" + efficiency +
                '}';
    }
}





