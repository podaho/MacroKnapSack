package com.pogrammerlin.macroknapsack.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("application")
public class GlobalProperties {
    private String myName;
}
