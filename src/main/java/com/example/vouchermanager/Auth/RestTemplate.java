package com.example.vouchermanager.Auth;

import org.springframework.context.annotation.Bean;

public class RestTemplate {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
