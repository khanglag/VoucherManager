package com.example.vouchermanager.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class VoucherapplicableproductId implements java.io.Serializable {
    private static final long serialVersionUID = -9011161682912615929L;
    @Column(name = "VoucherCode", nullable = false, length = 50)
    private String voucherCode;

    @Column(name = "ProductID", nullable = false)
    private Integer productID;
<<<<<<< HEAD
=======

    public VoucherapplicableproductId(String voucherCode, Long productId) {
        this.voucherCode = voucherCode;
        this.productID = Math.toIntExact(productId);
    }

    public VoucherapplicableproductId() {

    }

>>>>>>> main
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VoucherapplicableproductId entity = (VoucherapplicableproductId) o;
        return Objects.equals(this.productID, entity.productID) &&
                Objects.equals(this.voucherCode, entity.voucherCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productID, voucherCode);
    }

}