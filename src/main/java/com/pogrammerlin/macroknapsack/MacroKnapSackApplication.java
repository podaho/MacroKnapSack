package com.pogrammerlin.macroknapsack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableConfigurationProperties
public class MacroKnapSackApplication {

    public static void main(String[] args) {
        SpringApplication.run(MacroKnapSackApplication.class, args);
    }

}
