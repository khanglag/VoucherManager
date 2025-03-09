package com.example.vouchermanager.Model.DTO;

import com.example.vouchermanager.Model.Enum.VoucherStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VoucherDTO {
    private String voucherCode;
    private String discountType;
    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minimumOrderValue;
    private VoucherStatus status;
    private int createBy;
    private int usageCount;
    private int maxUsage;
    private LocalDateTime createdDate;
    private boolean applycableForAllProducts;

    @Override
    public String toString() {
        return "VoucherDTO{" +
                "voucherCode='" + voucherCode + '\'' +
                ", discountType='" + discountType + '\'' +
                ", discountValue=" + discountValue +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", minimumOrderValue=" + minimumOrderValue +
                ", status=" + status +
                ", createdBy=" + createBy +
                ", usageCount=" + usageCount +
                ", maxUsage=" + maxUsage +
                ", createdDate=" + createdDate +
                ", applicableForAllProducts=" + applycableForAllProducts +
                '}';
    }

}
