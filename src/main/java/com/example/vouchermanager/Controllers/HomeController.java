package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.DTO.CartDTO;
import com.example.vouchermanager.Model.Entity.Product;
import com.example.vouchermanager.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;

    @RequestMapping("/vouchers")
    public String vouchers() {
        return "user/vouchers";
    }

    @RequestMapping("/store")
    public String store() {
        return "user/store";
    }

    @RequestMapping("/brands")
    public String brands() {
        return "user/brand_collaborations";
    }

    @RequestMapping("/payment")
    public String payment() {
        return "user/payment";
    }


    @RequestMapping("/contact")
    public String contact() {
        return "user/contact";
    }

    @RequestMapping("/auth")
    public String login() {
        return "auth";
    }

    @RequestMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/index")
    public String checkLoginIndexPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);

            if (user != null) {
                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
            }
        }
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cart", cart);
        return "index";
    }

    @GetMapping("/brands")
    public String checkLoginBrandsPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
            }
        }
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>(); // Nếu cart chưa có, tạo danh sách rỗng để tránh lỗi
        }
        model.addAttribute("cart", cart);
        return "user/brand_collaborations";
    }

    @GetMapping("/payment")
    public String checkLoginPaymentPage(Model model, Authentication authentication, HttpSession session) {
        // Xử lý thông tin người dùng
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
            }
        }
        List<CartDTO> carts = new ArrayList<>();
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart != null) {
            for (int i = 0; i < cart.size(); i++) {
                CartDTO cartDTO = new CartDTO(i, cart.get(i), 1);
                carts.add(cartDTO); // Add to list
            }
        }
        List<String> vouchers = (List<String>) session.getAttribute("voucher");
        if (vouchers == null) {
            vouchers = new ArrayList<>(); // Khởi tạo danh sách rỗng
            session.setAttribute("voucher", vouchers);
        }
        vouchers = new ArrayList<>(); // Khởi tạo danh sách rỗng
        session.setAttribute("voucher", vouchers);
        model.addAttribute("carts", carts);
        session.setAttribute("cartpayment", carts);
        return "user/payment";
    }

    @GetMapping("/contact")
    public String checkLoginContactPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            com.example.vouchermanager.Model.Entity.User user = userService.findByUsername(username);
            if (user != null) {
                model.addAttribute("user", username);
                model.addAttribute("name", user.getFullName());
            }
        }
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        model.addAttribute("cart", cart);
        return "user/contact";
    }
}
