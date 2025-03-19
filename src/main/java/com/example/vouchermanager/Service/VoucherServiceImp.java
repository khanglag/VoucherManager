package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherDTO;
import com.example.vouchermanager.Repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VoucherServiceImp implements VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    public List<VoucherDTO> findAll() {
        return voucherRepository.findAll().stream()
                .map(voucher -> new VoucherDTO(
                        voucher.getVoucherCode(),
                        voucher.getTitle(),
                        voucher.getLogoUrl(),
                        voucher.getDescription(),
                        voucher.getDiscountType(),
                        voucher.getDiscountValue(),
                        voucher.getStartDate(),
                        voucher.getEndDate(),
                        voucher.getMinimumOrderValue(),
                        voucher.getStatus(),
                        voucher.getCreatedBy().getId(),
                        voucher.getUsageCount(),
                        voucher.getMaxUsage(),
                        convertInstantToLocalDateTime(voucher.getCreatedDate()),
                        voucher.getApplicableForAllProducts()
                ))
                .collect(Collectors.toList());
    }

    private LocalDateTime convertInstantToLocalDateTime(Instant instant) {
        return instant != null ? LocalDateTime.ofInstant(instant, ZoneId.systemDefault()) : null;
    }
}
