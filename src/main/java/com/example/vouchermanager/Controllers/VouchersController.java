package com.example.vouchermanager.Controllers;
import org.springframework.ui.Model;
import com.example.vouchermanager.Model.DTO.VoucherDTO;
import com.example.vouchermanager.Service.VoucherServiceImp;
import com.example.vouchermanager.Service.VoucherusageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class VouchersController {
    @Autowired
    private VoucherServiceImp voucherServiceImp;

    @GetMapping("/vouchers")
    public String vouchers(Model model) {
        // Lấy danh sách voucher từ service
        List<VoucherDTO> vouchers = voucherServiceImp.findAll();


        model.addAttribute("vouchers", vouchers);
        System.out.println("==================================== voucher");
        System.out.println(vouchers);
        return "user/vouchers"; // Trả về trang vouchers.html
    }
}
