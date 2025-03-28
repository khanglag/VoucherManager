package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.DTO.VoucherApplicableProductDTO;
import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Model.Entity.Voucherapplicableproduct;
import com.example.vouchermanager.Model.Entity.VoucherapplicableproductId;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherApplicableProductRepository extends JpaRepository<Voucherapplicableproduct, VoucherapplicableproductId> {
    @Query("SELECT v FROM Voucherapplicableproduct vap JOIN vap.voucherCode v WHERE vap.productID.id = :productId")
    List<Voucher> findVouchersByProductId(@Param("productId") Integer productId);
}