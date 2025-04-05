package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.DTO.CartDTO;
import com.example.vouchermanager.Model.DTO.VoucherDTO;
import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
@CrossOrigin(origins = "*") // Cho phép FE gọi API từ domain khác
public class VoucherAPIController {
    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public List<VoucherDTO> getVouchersByType(@RequestParam String type, HttpSession session) {
        List<CartDTO> cartDTOS =(List<CartDTO>) session.getAttribute("cartpayment");
        List<Integer> list = new ArrayList<>();
        BigDecimal total = BigDecimal.valueOf(0.0);
        for (CartDTO cartDTO : cartDTOS) {
            list.add(cartDTO.getId());
            total=total.add(cartDTO.getProduct().getPrice().multiply(BigDecimal.valueOf(cartDTO.getQuantity())))  ;
        }
        List<VoucherDTO> vouchers = new ArrayList<>();
        if (type.equals("shop-voucher")){
            for (Voucher voucher: voucherService.getSortedDiscountVouchers(list,total))
            {
                vouchers.add( VoucherDTO.fromEntity(voucher));
            };
            return vouchers;
        }
        for (Voucher voucher: voucherService.getSortedFreeShipVouchers(list,total))
        {
            vouchers.add( VoucherDTO.fromEntity(voucher));
        };
        return vouchers;
    }
}
