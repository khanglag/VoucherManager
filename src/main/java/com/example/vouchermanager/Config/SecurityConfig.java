package com.example.vouchermanager.Config;

import com.example.vouchermanager.Auth.CustomAuthenticationFailureHandler;
import com.example.vouchermanager.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.example.vouchermanager.Auth.CustomAuthenticationSuccessHandler;
import com.example.vouchermanager.Auth.CustomOAuth2SuccessHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
            CustomOAuth2SuccessHandler customOAuth2SuccessHandler) {
        this.customUserDetailsService = customUserDetailsService;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // .csrf(csrf -> csrf.disable())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/auth/**","/store/**","/admin/**"))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth", "/", "/index", "/static/**", "/assets/**", "/templates/**",
                                "/user/**", "/vouchers", "/store", "/brands", "/register", "/auth/register",
                                "/order-details/**")

                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/auth") // Chỉ định trang đăng nhập
                        .permitAll()
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl("/auth?error")
                        .failureHandler(authenticationFailureHandler())
                        .loginProcessingUrl("/j_spring_security_check"))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/auth") // Trang đăng nhập tùy chỉnh
                        .successHandler(customOAuth2SuccessHandler)
                        .failureUrl("/auth?error") // Trang đích khi đăng nhập thất bại
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
