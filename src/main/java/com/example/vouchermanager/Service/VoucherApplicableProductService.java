package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherApplicableProductDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VoucherApplicableProductService {
    List<VoucherApplicableProductDTO> findAll();
}