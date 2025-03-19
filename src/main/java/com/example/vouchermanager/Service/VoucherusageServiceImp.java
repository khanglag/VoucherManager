package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherUsageDTO;
import com.example.vouchermanager.Repository.VoucherusageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherusageServiceImp implements VoucherusageService {
    @Autowired
    private VoucherusageRepository voucherusageRepository;

    @Override
    public List<VoucherUsageDTO> findAll() {
        return voucherusageRepository.findAll().stream()
                .map(voucherusage -> new VoucherUsageDTO(
                        voucherusage.getId(),
                        voucherusage.getOrderID().getId(),
                        voucherusage.getVoucherCode().getVoucherCode(),
                        convertInstantToLocalDateTime(voucherusage.getUsedDate())
                ))
                .collect(Collectors.toList());
    }

    private LocalDateTime convertInstantToLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }
}
