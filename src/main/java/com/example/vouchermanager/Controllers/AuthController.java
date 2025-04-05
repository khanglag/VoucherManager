package com.example.vouchermanager.Controllers;

import com.example.vouchermanager.Model.Entity.Role;
import com.example.vouchermanager.Model.Entity.User;
import com.example.vouchermanager.Service.UserService;
import com.example.vouchermanager.Service.UserServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RequestMapping("/auth")
@Controller
public class AuthController {
    @Autowired
    private UserServiceImp userServiceImp;



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        String numberPhone = user.getPhoneNumber();
        String email = user.getEmail();

        if (userServiceImp.findByPhoneNumber(numberPhone) != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Số điện thoại đã được sử dụng!"));
        }

        if (userServiceImp.findByEmail(email) != null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email đã được sử dụng!"));
        }

        Role role = new Role();
        role.setId(3);
        user.setRoleID(role);
        user.setStatus(true);
        userServiceImp.createUser(user);

        return ResponseEntity.ok(Map.of("message", "Đăng ký thành công!"));
    }
}
