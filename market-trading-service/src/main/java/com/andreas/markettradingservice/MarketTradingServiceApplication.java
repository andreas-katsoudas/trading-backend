package com.andreas.markettradingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MarketTradingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketTradingServiceApplication.class, args);
	}

}
