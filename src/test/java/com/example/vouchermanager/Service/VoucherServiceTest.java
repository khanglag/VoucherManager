package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherDTO;
import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Model.Enum.DiscountType;
import com.example.vouchermanager.Model.Enum.VoucherStatus;
import com.example.vouchermanager.Repository.VoucherRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Chạy test theo thứ tự
public class VoucherServiceTest {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    // Test tạo voucher
    @Test
    @Order(1)
    @Rollback(value = false)
    public void testCreateVoucher() {
        Voucher voucher = new Voucher();
        voucher.setVoucherCode("SALE11");
        voucher.setTitle("Giảm 10%");
        voucher.setDiscountType(DiscountType.PERCENTAGE);
        voucher.setDiscountValue(new BigDecimal("10.00"));
        voucher.setStartDate(LocalDate.now());
        voucher.setEndDate(LocalDate.now().plusDays(30));
        voucher.setMaxUsage(21);
        voucher.setUsageCount(1);
        voucher.setStatus(VoucherStatus.ACTIVE);
        Voucher savedVoucher = voucherService.create(voucher);
        assertNotNull(savedVoucher);
        assertEquals("SALE10", savedVoucher.getVoucherCode());
    }

    // Test lấy danh sách voucher
    @Test
    @Order(2)
    public void testFindAllVouchers() {
        List<VoucherDTO> vouchers = voucherService.findAll();
        assertFalse(vouchers.isEmpty());
    }

    // Test lấy voucher theo ID
    @Test
    @Order(3)
    public void testGetVoucherById() {
        Optional<Voucher> voucher = voucherService.getById("SALE10");
        assertTrue(voucher.isPresent());
        assertEquals("Giảm 10%", voucher.get().getTitle());
    }

    // Test cập nhật voucher
    @Test
    @Order(4)
    @Rollback(value = false)
    public void testUpdateVoucher() {
        Optional<Voucher> existingVoucher = voucherService.getById("SALE10");
        assertTrue(existingVoucher.isPresent());

        Voucher voucher = existingVoucher.get();
        voucher.setTitle("Giảm 10% toàn bộ sản phẩm");
        voucherService.update("SALE10", voucher);

        Optional<Voucher> updatedVoucher = voucherService.getById("SALE10");
        assertTrue(updatedVoucher.isPresent());
        assertEquals("Giảm 10% toàn bộ sản phẩm", updatedVoucher.get().getTitle());
    }

    // Test xóa voucher
    @Test
    @Order(5)
    @Rollback(value = false)
    public void testDeleteVoucher() {
        voucherService.delete("SALE10");
        Optional<Voucher> deletedVoucher = voucherService.getById("SALE10");
        assertFalse(deletedVoucher.isPresent());
    }
}