package com.mortgages.ai.mortgageservices.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties(prefix = "document")
public class DocumentConfig {

    public List<String> allowedMimeTypes;
    public int maxFileSizeMb;
    public String gcsBucket;
    public String aiAgentUrl;
}
