//package com.example.vouchermanager.Service;
//
//import com.example.vouchermanager.Model.DTO.VoucherUsageDTO;
//import com.example.vouchermanager.Model.Entity.Voucher;
//import com.example.vouchermanager.Model.Entity.Voucherusage;
//import com.example.vouchermanager.Repository.OrderRepository;
//import com.example.vouchermanager.Repository.VoucherusageRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.Instant;
//import java.util.List;
//
//@SpringBootTest
//public class VoucherusageServiceImpTest {
//    @Autowired
//    VoucherusageServiceImp voucherusageServiceImp;
//    @Autowired
//    OrderRepository orderRepository;
//    @Autowired
//    VoucherService voucherService;
//    @Autowired
//    VoucherusageRepository voucherusageRepository;
//
//    @Test
//    public void testFindAll(){
//        List<VoucherUsageDTO> voucherUsageDTOS = voucherusageServiceImp.findAll();
//        voucherUsageDTOS.forEach(System.out::println);
//    }
//    @Test
//    public void save(){
//        Voucherusage voucherusage = new Voucherusage();
//        voucherusage.setVoucherCode(voucherService.getById("SALE11").get());
//        voucherusage.setOrderID(orderRepository.findById(29).get());
//        voucherusage.setUsedDate(Instant.now());
//        voucherusageRepository.save(voucherusage);
//    }
//
//}
