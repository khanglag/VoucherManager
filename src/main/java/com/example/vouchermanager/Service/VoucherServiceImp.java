package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherDTO;
import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
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
    @Override
    public Optional<Voucher> getById(String voucherCode) {
        return voucherRepository.findById(voucherCode);
    }

    @Override
    public Voucher create(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public Voucher update(String voucherCode, Voucher voucher) {
        if (voucherRepository.existsById(voucherCode)) {
            return voucherRepository.save(voucher);
        }
        throw new RuntimeException("Voucher not found");
    }

    @Override
    public void delete(String voucherCode) {
        voucherRepository.deleteById(voucherCode);
    }

    @Override
    public Page<VoucherDTO> findAllFiltered(String title, BigDecimal discountValue, String status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<Voucher> spec = VoucherSpecification.filterVouchers(title, discountValue, status, startDate, endDate);
        return voucherRepository.findAll(spec, pageable).map(
                voucher -> new VoucherDTO(voucher.g, voucher.getTitle())
        );
    }
}
