package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherApplicableProductDTO;
<<<<<<< HEAD
import org.springframework.stereotype.Service;

import java.util.List;
=======
import com.example.vouchermanager.Model.Entity.Voucherapplicableproduct;
import com.example.vouchermanager.Model.Entity.VoucherapplicableproductId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
>>>>>>> main

@Service
public interface VoucherApplicableProductService {
    List<VoucherApplicableProductDTO> findAll();
<<<<<<< HEAD
=======

    Optional<Voucherapplicableproduct> getById(VoucherapplicableproductId id);

    Voucherapplicableproduct create(Voucherapplicableproduct voucherApplicableProduct);

    Voucherapplicableproduct update(VoucherapplicableproductId id, Voucherapplicableproduct voucherApplicableProduct);

    void delete(VoucherapplicableproductId id);
>>>>>>> main
}