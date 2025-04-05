package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherUsageDTO;
<<<<<<< HEAD
=======
import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Model.Entity.Voucherusage;
import com.example.vouchermanager.Repository.OrderRepository;
import com.example.vouchermanager.Repository.VoucherusageRepository;
>>>>>>> main
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

<<<<<<< HEAD
=======
import java.time.Instant;
>>>>>>> main
import java.util.List;

@SpringBootTest
public class VoucherusageServiceImpTest {
    @Autowired
    VoucherusageServiceImp voucherusageServiceImp;
<<<<<<< HEAD
=======
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    VoucherService voucherService;
    @Autowired
    VoucherusageRepository voucherusageRepository;
>>>>>>> main

    @Test
    public void testFindAll(){
        List<VoucherUsageDTO> voucherUsageDTOS = voucherusageServiceImp.findAll();
        voucherUsageDTOS.forEach(System.out::println);
    }
<<<<<<< HEAD
=======
    @Test
    public void save(){
        Voucherusage voucherusage = new Voucherusage();
        voucherusage.setVoucherCode(voucherService.getById("SALE11").get());
        voucherusage.setOrderID(orderRepository.findById(29).get());
        voucherusage.setUsedDate(Instant.now());
        voucherusageRepository.save(voucherusage);
    }
    
>>>>>>> main
}
