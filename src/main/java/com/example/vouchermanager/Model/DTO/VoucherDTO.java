package com.example.vouchermanager.Model.DTO;

import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Model.Enum.DiscountType;
import com.example.vouchermanager.Model.Enum.VoucherStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VoucherDTO {
    private String voucherCode;
    private String title;
    private String logoUrl;
    private String description;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minimumOrderValue;
    private VoucherStatus status;
    private int createBy;
    private int usageCount;
    private int maxUsage;
    private LocalDateTime createdDate;
    private boolean applicableForAllProducts;

    @Override
    public String toString() {
        return "VoucherDTO{" +
                "voucherCode='" + voucherCode + '\'' +
                "Title='" + title + '\'' +
                "logoUrl='" + logoUrl + '\'' +
                "description='" + description + '\'' +
                ", discountType='" + discountType + '\'' +
                ", discountValue=" + discountValue +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", minimumOrderValue=" + minimumOrderValue +
                ", status=" + status +
                ", createBy=" + createBy +
                ", usageCount=" + usageCount +
                ", maxUsage=" + maxUsage +
                ", createdDate=" + createdDate +
                ", applicableForAllProducts=" + applicableForAllProducts +
                '}';
    }
    public static VoucherDTO fromEntity(Voucher voucher) {
        return new VoucherDTO(
                voucher.getVoucherCode(),
                voucher.getTitle(),
                voucher.getLogoUrl(),
                voucher.getDescription(),
                voucher.getDiscountType(),
                voucher.getDiscountValue(),
                voucher.getStartDate(),
                voucher.getEndDate(),
                voucher.getMinimumOrderValue(),
                voucher.getStatus(),
                voucher.getCreatedBy() != null ? voucher.getCreatedBy().getId() : 0,
                voucher.getUsageCount(),
                voucher.getMaxUsage(),
                voucher.getCreatedDate() != null ? voucher.getCreatedDate().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay() : null,
                voucher.getApplicableForAllProducts()
        );
    }
}
