package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VoucherService {
    List<VoucherDTO> findAll();
}
