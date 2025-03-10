package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.OrderDTO;
import com.example.vouchermanager.Model.DTO.VoucherApplicableProductDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class VoucherApplicableProductServiceImpTest {
    @Autowired
    VoucherApplicableProductServiceImp voucherApplicableProductServiceImp;

    @Test
    public void testFindAll(){
        List<VoucherApplicableProductDTO> voucherApplicableProductDTOS = voucherApplicableProductServiceImp.findAll();
        voucherApplicableProductDTOS.forEach(voucher -> System.out.println(voucher));
    }
}
