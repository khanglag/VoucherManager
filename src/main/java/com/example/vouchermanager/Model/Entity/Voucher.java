package com.example.vouchermanager.Model.Entity;

import com.example.vouchermanager.Model.Enum.VoucherStatus;
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

    @Lob
    @Column(name = "DiscountType", nullable = false)
    private String discountType;

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

}