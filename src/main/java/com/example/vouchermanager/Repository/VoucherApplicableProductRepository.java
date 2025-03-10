package com.example.vouchermanager.Repository;

import com.example.vouchermanager.Model.DTO.VoucherApplicableProductDTO;
import com.example.vouchermanager.Model.Entity.Voucherapplicableproduct;
import com.example.vouchermanager.Model.Entity.VoucherapplicableproductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherApplicableProductRepository extends JpaRepository<Voucherapplicableproduct, VoucherapplicableproductId> {

}