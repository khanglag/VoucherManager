package com.example.vouchermanager.Auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Sai tài khoản hoặc mật khẩu!";

        if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "Tài khoản đã bị khóa!";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Sai mật khẩu!";
        }

        response.sendRedirect(response.encodeRedirectURL("/signin?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8)));

    }
}
