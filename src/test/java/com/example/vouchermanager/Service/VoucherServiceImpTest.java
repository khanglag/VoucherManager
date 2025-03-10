package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class VoucherServiceImpTest {
    @Autowired
    VoucherServiceImp voucherServiceImp;

    @Test
    public void testFindAll(){
        List<VoucherDTO> voucherDTOS = voucherServiceImp.findAll();
        voucherDTOS.forEach(System.out::println);
    }
}
