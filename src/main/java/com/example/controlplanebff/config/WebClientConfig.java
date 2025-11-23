package com.example.controlplanebff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient controlPlaneApiWebClient(@Value("${control-plane.api-base-url}") String apiBaseUrl) {
        return WebClient.builder()
                .baseUrl(apiBaseUrl)
                .build();
    }
}


