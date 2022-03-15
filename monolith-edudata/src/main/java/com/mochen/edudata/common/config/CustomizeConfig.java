package com.mochen.edudata.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class CustomizeConfig {
    @Value("${customize.jwt.secret}")
    private String jwtSecret;

    @Value("${customize.sm4.key}")
    private String sm4key;
}
