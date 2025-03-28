package com.example.vouchermanager.Controllers;
import com.example.vouchermanager.Model.Entity.Product;
import com.example.vouchermanager.Model.Entity.Role;
import com.example.vouchermanager.Model.Entity.User;
import com.example.vouchermanager.Model.Entity.Voucher;
import com.example.vouchermanager.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import com.example.vouchermanager.Model.DTO.VoucherDTO;
import com.example.vouchermanager.Service.VoucherServiceImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class VouchersController {
    @Autowired
    private VoucherServiceImp voucherServiceImp;

    @Autowired
    private UserService userService;

    @GetMapping("/vouchers")
    public String vouchers(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
            }
        }
        List<VoucherDTO> vouchers = voucherServiceImp.findAll();
        model.addAttribute("vouchers", vouchers);

        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cart", cart);
        return "user/vouchers";
    }


}
