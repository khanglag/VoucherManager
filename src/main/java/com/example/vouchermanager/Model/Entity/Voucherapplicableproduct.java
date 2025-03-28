package com.example.vouchermanager.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "voucherapplicableproducts")
public class Voucherapplicableproduct {
    @EmbeddedId
    private VoucherapplicableproductId id;

    @MapsId("voucherCode")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "VoucherCode", nullable = false)
    private Voucher voucherCode;

    @MapsId("productID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ProductID", nullable = false)
    private Product productID;

    public Voucherapplicableproduct(VoucherapplicableproductId id, Voucher voucher, Product product) {
        this.id = id;
        this.voucherCode = voucher;
        this.productID = product;
    }

    public Voucherapplicableproduct() {

    }
}