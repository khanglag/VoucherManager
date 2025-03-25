package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.Entity.Voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String>, JpaSpecificationExecutor<Voucher> {
    @Query("""
        SELECT v FROM Voucher v
        JOIN Voucherapplicableproduct vap ON v.voucherCode = vap.id.voucherCode
        WHERE vap.id.productID = :productId
        AND v.status = 'ACTIVE'
        AND v.discountType = 'PERCENTAGE'
    """)
    List<Voucher> findPercentageVouchersByProduct(@Param("productId") Integer productId);

    @Query("""
        SELECT v FROM Voucher v
        JOIN Voucherapplicableproduct vap ON v.voucherCode = vap.id.voucherCode
        WHERE vap.id.productID = :productId
        AND v.status = 'ACTIVE'
        AND v.discountType = 'FIXED'
    """)
    List<Voucher> findFixedVouchersByProduct(@Param("productId") Integer productId);

    @Query("""
        SELECT v FROM Voucher v
        JOIN Voucherapplicableproduct vap ON v.voucherCode = vap.id.voucherCode
        WHERE vap.id.productID = :productId
        AND v.status = 'ACTIVE'
        AND v.discountType = 'FREESHIP'
    """)
    List<Voucher> findFreeShipVouchersByProduct(@Param("productId") Integer productId);
}
