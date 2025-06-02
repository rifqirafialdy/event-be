package com.example.event_be;

import com.example.event_be.auth.infrastructure.security.JwtConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfigProperties.class)

public class EventBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventBeApplication.class, args);
	}

}
