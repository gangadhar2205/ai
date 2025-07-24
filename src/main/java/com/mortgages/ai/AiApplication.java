package com.mortgages.ai;

import com.mortgages.ai.authentication.config.JwtKeyProps;
import com.mortgages.ai.mortgageservices.config.DocumentConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.mortgages.ai")
@EnableFeignClients(basePackages = "com.mortgages.ai.mortgageservices.feigns")
@EnableConfigurationProperties({
		JwtKeyProps.class,
		DocumentConfig.class
})
public class AiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiApplication.class, args);
	}

}
