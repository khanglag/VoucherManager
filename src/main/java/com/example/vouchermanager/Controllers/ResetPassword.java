package com.example.vouchermanager.Controllers;


import com.example.vouchermanager.Model.Entity.User;
import com.example.vouchermanager.Service.EmailService;
import com.example.vouchermanager.Service.UserServiceImp;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;

@Controller
public class ResetPassword {
    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private EmailService emailService;

    @RequestMapping("/reset-password")
    public String forgetPassword() {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email, HttpSession session, Model model) throws MessagingException {
        User user = userServiceImp.findByEmail(email);
        System.out.println(user);
        if (user == null) {
            model.addAttribute("error", "Không tìm thấy người dùng có email này");
            System.out.println("lỗi");
            return "reset-password";
        }
        String otp = generateOTP();
        System.out.println(otp);
        session.setAttribute("email", email);
        session.setAttribute("otp", otp);
        emailService.sendOTP(email,user.getFullName(),otp);
        model.addAttribute("message", "OTP mới đã được gửi!");
        System.out.println("OTP lưu vào session: " + session.getAttribute("otp"));
        return "confirm_OTP";
    }

    @PostMapping("/verify-OTP")
    public String verifyOTP(@RequestParam("otp") String enteredOTP, HttpSession session, Model model) throws MessagingException {
        String storedOTP = (String) session.getAttribute("otp");
        String email = (String) session.getAttribute("email");
        System.out.println(storedOTP);
        if (storedOTP == null || !storedOTP.equals(enteredOTP)) {
            model.addAttribute("error", "OTP không chính xác hoặc đã hết hạn");

            System.out.println("sai");
            return "confirm_OTP";
        }

        System.out.println("đung");
        String newPassword = generateNewPassword();
        System.out.println("mat khau ne: " + newPassword);
        User user = userServiceImp.findByEmail(email);
        userServiceImp.forgetPassword(user.getId(),newPassword);
        emailService.sendNewPasswordEmail(email,user.getFullName(),newPassword);
        session.removeAttribute("otp");
        session.removeAttribute("email");

        return "auth"; // Trang thành công
    }

    @GetMapping("resend-OTP")
    public String resendOTP(HttpSession session, Model model) {
        session.removeAttribute("otp");
        String email = (String) session.getAttribute("email");
        if (email == null) {
            return "redirect:/reset-password";
        }
        String otp = generateOTP();
        session.setAttribute("otp", otp);
        System.out.println("otp mới" + otp);

        model.addAttribute("message", "OTP mới đã được gửi!");
        return "confirm_OTP";

    }
    private static String generateOTP() {
        int length = 6;
        String characters = "0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }

    private static String generateNewPassword() {
        int length = 8;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }

}
