package com.example.vouchermanager.Model.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/signin")
    public String login() {
        return "login";
    }

    @RequestMapping("/index")
    public String home() {
        return "index";
    }
}
