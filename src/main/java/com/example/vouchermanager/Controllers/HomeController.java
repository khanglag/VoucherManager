package com.example.vouchermanager.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {


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

    @RequestMapping("/signin")
    public String login() {
        return "login";
    }

    @RequestMapping("/index")
    public String home() {
        return "index";
    }
}
