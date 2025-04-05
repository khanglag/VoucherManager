package com.example.vouchermanager.Model.Entity;

import com.example.vouchermanager.Model.Enum.DiscountType;
import com.example.vouchermanager.Model.Enum.VoucherStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "voucher")
public class Voucher {
    @Id
    @Column(name = "VoucherCode", nullable = false, length = 50)
    private String voucherCode;

    @Column(name = "Title")
    private String title;

    @Column(name = "LogoUrl")
    private String logoUrl;

    @Column(name = "Description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Lob
    @Column(name = "DiscountType", nullable = false)
    private DiscountType discountType;

    @Column(name = "DiscountValue", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "MinimumOrderValue", precision = 10, scale = 2)
    private BigDecimal minimumOrderValue;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACTIVE'")
    @Lob
    @Column(name = "Status", nullable = false)
    private VoucherStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "CreatedBy")
    private User createdBy;

    @ColumnDefault("0")
    @Column(name = "UsageCount", nullable = false)
    private Integer usageCount;

    @ColumnDefault("100")
    @Column(name = "MaxUsage", nullable = false)
    private Integer maxUsage;

    @ColumnDefault("current_timestamp()")
    @Column(name = "CreatedDate")
    private Instant createdDate;

    @ColumnDefault("0")
    @Column(name = "ApplicableForAllProducts", nullable = false)
    private Boolean applicableForAllProducts = false;

    @Override
    public String toString() {
        return "Voucher{" +
                "voucherCode='" + voucherCode + '\'' +
                ", title='" + title + '\'' +
                ", logoUrl='" + logoUrl + '\'' +
                ", description='" + description + '\'' +
                ", discountType=" + discountType +
                ", discountValue=" + discountValue +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", minimumOrderValue=" + minimumOrderValue +
                ", status=" + status +
                ", createdBy=" + createdBy +
                ", usageCount=" + usageCount +
                ", maxUsage=" + maxUsage +
                ", createdDate=" + createdDate +
                ", applicableForAllProducts=" + applicableForAllProducts +
                '}';
    }
}