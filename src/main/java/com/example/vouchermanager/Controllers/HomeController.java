package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.util.List;

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

    @RequestMapping("/auth")
    public String login() {
        return "auth";
    }

    @RequestMapping("/index")
    public String home() {
        return "index";
    }


    @GetMapping("/index")
    public String checkLogin(Model model, Authentication authentication, HttpSession session) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            User user = (User) principal;
            String username = user.getUsername();
            com.example.vouchermanager.Model.Entity.User u = userService.findByUsername(username);
            String name=  u.getFullName();
            model.addAttribute("user", username);
            model.addAttribute("name", name);
        }
        return "index";
    }
}
