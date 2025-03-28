package com.example.vouchermanager.Auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("=======================");

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            System.out.println(role);
            if (role.equals("ROLE_Admin")) {
                response.sendRedirect("/admin");
                return;
            }else {
                response.sendRedirect("/index");
                return;
            }
        }
        response.sendRedirect("/access-denied");
    }
}
