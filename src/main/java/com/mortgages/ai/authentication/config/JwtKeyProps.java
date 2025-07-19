package com.mortgages.ai.authentication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtKeyProps {
    private String privateKey;
    private String publicKey;
    private Integer keySpecs;
}
