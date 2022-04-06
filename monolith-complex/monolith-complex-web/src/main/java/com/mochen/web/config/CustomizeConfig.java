package com.mochen.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
@ConfigurationProperties(prefix = "customize")
@Data
public class CustomizeConfig {

    private String jwtSecret;

    private String sm4key;
}
