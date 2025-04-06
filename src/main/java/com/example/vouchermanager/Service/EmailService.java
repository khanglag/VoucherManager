package com.example.vouchermanager.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("khangdo0107@gmail.com"); // Email gửi
        mailSender.send(message);
    }
    public void sendNewPasswordEmail(String to, String name, String newPassword) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("newPassword", newPassword);

        String emailContent = templateEngine.process("new-password-template", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setFrom("bookingcare6868@gmail.com");
        helper.setSubject("Your New Password");
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    public void sendOTP(String to, String name, String otp) throws MessagingException {
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("otp", otp);

        String emailContent = templateEngine.process("OTP-template", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setFrom("bookingcare6868@gmail.com");
        helper.setSubject("OTP");
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }
}
