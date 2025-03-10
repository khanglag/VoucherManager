package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherUsageDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VoucherusageService {
    List<VoucherUsageDTO> findAll();
}
