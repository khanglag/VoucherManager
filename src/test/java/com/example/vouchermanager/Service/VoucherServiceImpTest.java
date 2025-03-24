package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherDTO;
import com.example.vouchermanager.Model.Enum.DiscountType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
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
//    @Test
//    public void testfindAllFiltered(){
//        VoucherDTO voucherDTO = new VoucherDTO();
//        voucherDTO.setVoucherCode("FIXED50");
//        voucherDTO.setTitle("Giảm 50k đơn từ 100k");
//        voucherDTO.setDiscountType(DiscountType.FIXED);
//        voucherDTO.setDiscountValue(BigDecimal.valueOf(50000));
//        List<VoucherDTO> mockVoucherList = Collections.singletonList(voucherDTO);
//        Page<VoucherDTO> mockPage = new PageImpl<>(mockVoucherList);
//
//
//
//
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<VoucherDTO> result = voucherServiceImp.findAllFiltered(
//                voucherDTO.getTitle(),
//                voucherDTO.getDiscountValue(),
//                String.valueOf(voucherDTO.getStatus()),
//                voucherDTO.getStartDate(),
//                voucherDTO.getEndDate(),
//                pageable
//        );
//
//        System.out.println("=== KẾT QUẢ TÌM ĐƯỢC ===");
//        result.forEach(System.out::println);
//
//    }

}
