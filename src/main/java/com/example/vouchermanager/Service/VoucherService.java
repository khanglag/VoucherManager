package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.*;
import com.example.vouchermanager.Model.Entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface VoucherService {
    List<VoucherDTO> findAll();

    Page<VoucherDTO> getAllVouchers(org.springframework.data.domain.Pageable pageable);

    Optional<Voucher> getById(String voucherCode);

    Voucher create(Voucher voucher);

    Voucher update(String voucherCode, Voucher voucher);

    void delete(String voucherCode);


    Page<VoucherDTO> findAllFiltered(String title, BigDecimal discountValue, String status, LocalDate startDate,
            LocalDate endDate, org.springframework.data.domain.Pageable pageable);

    VoucherCreationResultDTO createVoucherWithCustomCode(Voucher voucher);

    VoucherUsageResultDTO useVoucher(String voucherCode, BigDecimal orderValue);

    VoucherActivationResultDTO activateVoucher(String voucherCode);

    VoucherDeactivationResultDTO deactivateVoucher(String voucherCode);

    Page<VoucherDTO> findByApplicableProducts(String productId, Pageable pageable);

    Voucher create(Voucher voucher, MultipartFile logoFile);

    List<Voucher> getPercentageVouchers(Integer productId);

    List<Voucher> getFixedVouchers(Integer productId);

    List<Voucher> getFreeShipVouchers(Integer productId);

    void createVouchersForProducts(Voucher voucher, List<Integer> productIds);

    List<Voucher> getSortedDiscountVouchers(Integer productId);

    List<Voucher> getSortedFreeShipVouchers(Integer productId);
}
