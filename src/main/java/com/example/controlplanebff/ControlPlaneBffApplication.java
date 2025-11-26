package com.example.controlplanebff;

import com.example.controlplanebff.config.MarketLiveStreamGatewayProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MarketLiveStreamGatewayProperties.class)
public class ControlPlaneBffApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlPlaneBffApplication.class, args);
    }
}



