package com.pogrammerlin.macroknapsack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableConfigurationProperties
@EnableFeignClients
@EnableWebMvc
public class MacroKnapSackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MacroKnapSackApplication.class, args);
    }

}
