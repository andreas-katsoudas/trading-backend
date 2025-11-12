package com.andreas.marketdataservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "marketdata")
public class MarketDataProperties {
    private List<String> symbols;
    private long updateIntervalMs;
    private double volatility;
}