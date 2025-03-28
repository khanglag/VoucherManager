package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDTO;
import com.example.vouchermanager.Model.DTO.VoucherApplicableProductDTO;
import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Model.Entity.Voucherapplicableproduct;
import com.example.vouchermanager.Model.Enum.DiscountType;
import com.example.vouchermanager.Model.Enum.VoucherStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class VoucherApplicableProductServiceImpTest {
    @Autowired
    VoucherApplicableProductServiceImp voucherApplicableProductServiceImp;
    @Autowired
    VoucherService voucherService;
    @Autowired
    UserService userService;
    @Test
    public void createVouchersForProducts(){
        Voucher voucher = new Voucher();
        voucher.setVoucherCode("HAHAHAA");
        voucher.setTitle("Đêm mơ màn, sale rộn ràng");
        voucher.setLogoUrl("123");
        voucher.setCreatedBy(userService.findById(2));
        voucher.setDescription("Sale một đống");
        voucher.setDiscountType(DiscountType.FIXED);
        voucher.setDiscountValue(BigDecimal.valueOf(2000));
        voucher.setStartDate(LocalDate.from(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()));
        voucher.setEndDate(Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(10));
        voucher.setMinimumOrderValue(BigDecimal.valueOf(20000));
        voucher.setStatus(VoucherStatus.ACTIVE);
        voucher.setMaxUsage(1000);
        voucher.setCreatedDate(Instant.now());
        List list = new ArrayList();
        list.add(1);
        list.add(5);
        voucherService.createVouchersForProducts(voucher,list);
    }
    @Test
    public void getSortedDiscountVouchers()
    {
        for(Voucher voucher: voucherService.getSortedDiscountVouchers(1))
        {
            System.out.println("VoucherCode: " + voucher.getVoucherCode() +
                    ", Title: " + voucher.getTitle() +
                    ", Discount: " + voucher.getDiscountValue() +
                    ", Start: " + voucher.getStartDate() +
                    ", End: " + voucher.getEndDate());
        }

    }
}
