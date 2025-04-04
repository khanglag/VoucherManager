
package com.example.vouchermanager.Service;

import com.example.vouchermanager.Model.DTO.VoucherDTO;

import com.example.vouchermanager.Model.DTO.VoucherPerformanceDTO;
import com.example.vouchermanager.Model.Entity.User;

import com.example.vouchermanager.Model.Entity.Voucher;

import com.example.vouchermanager.Model.Enum.DiscountType;
import com.example.vouchermanager.Repository.VoucherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.Arrays;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class VoucherServiceImpTest {
    @Autowired
    VoucherServiceImp voucherServiceImp;

    @Autowired
    VoucherService voucherService;

    @Autowired
    VoucherRepository voucherRepository;

    @Autowired
    UserServiceImp userServiceImp;

    @Test
    public void testFindAll() {

        List<VoucherPerformanceDTO> performanceDTOs = voucherServiceImp
                .calculateVoucherPerformanceForAllUsersWithRoleId2();
        System.out.println(performanceDTOs);
    }

    @Test
    public void testVoucher() {
        int total = voucherServiceImp.getRemainingUsageForActiveVouchers();
        System.out.println("-------------");
        System.out.println(total);
    }

    @Test
    void setVoucherServiceImp() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        List<Voucher> vouchers = voucherService.getSortedDiscountVouchers(list, BigDecimal.valueOf(300000));
        for (Voucher voucher : vouchers) {
            System.out.println("Voucher code: " + voucher.getVoucherCode() + " ");
        }
    }
    // package com.example.vouchermanager.Service;
    //
    // import com.example.vouchermanager.Model.DTO.VoucherDTO;
    // import com.example.vouchermanager.Model.Entity.Voucher;
    // import org.junit.jupiter.api.Test;
    // import org.springframework.beans.factory.annotation.Autowired;
    // import org.springframework.boot.test.context.SpringBootTest;
    //
    // import java.util.List;
    //
    // @SpringBootTest
    // public class VoucherServiceImpTest {
    // @Autowired
    // VoucherServiceImp voucherServiceImp;
    //

    // @Test
    // public void testfindAllFiltered(){
    // VoucherDTO voucherDTO = new VoucherDTO();
    // voucherDTO.setVoucherCode("FIXED50");
    // voucherDTO.setTitle("Giảm 50k đơn từ 100k");
    // voucherDTO.setDiscountType(DiscountType.FIXED);
    // voucherDTO.setDiscountValue(BigDecimal.valueOf(50000));
    // List<VoucherDTO> mockVoucherList = Collections.singletonList(voucherDTO);
    // Page<VoucherDTO> mockPage = new PageImpl<>(mockVoucherList);
    //
    //
    //
    //
    // Pageable pageable = PageRequest.of(0, 10);
    // Page<VoucherDTO> result = voucherServiceImp.findAllFiltered(
    // voucherDTO.getTitle(),
    // voucherDTO.getDiscountValue(),
    // String.valueOf(voucherDTO.getStatus()),
    // voucherDTO.getStartDate(),
    // voucherDTO.getEndDate(),
    // pageable
    // );
    //
    // System.out.println("=== KẾT QUẢ TÌM ĐƯỢC ===");
    // result.forEach(System.out::println);
    //
    // }

    @Test
    void testGetVoucherStatistics() {
        Map<String, Object> statistics = voucherServiceImp.getVoucherStatistics();

        System.out.println("\n===== Voucher Statistics =====");

        System.out.println("\nIssued Per Month:");
        List<Object[]> issuedPerMonth = (List<Object[]>) statistics.get("issuedPerMonth");
        issuedPerMonth
                .forEach(row -> System.out.println("Year: " + row[0] + ", Month: " + row[1] + ", Issued: " + row[2]));

        // Kiểm tra dữ liệu
        assertEquals(2, issuedPerMonth.size());

    }
}
