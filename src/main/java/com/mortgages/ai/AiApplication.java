package com.mortgages.ai;

import com.mortgages.ai.authentication.config.JwtKeyProps;
import com.mortgages.ai.mortgageservices.config.DocumentConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		JwtKeyProps.class,
		DocumentConfig.class
})
public class AiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiApplication.class, args);
	}

}
