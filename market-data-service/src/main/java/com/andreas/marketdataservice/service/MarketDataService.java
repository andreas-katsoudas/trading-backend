package com.andreas.marketdataservice.service;

import com.andreas.common.avro.MarketPriceAvro;
import com.andreas.marketdataservice.config.MarketDataProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
public class MarketDataService {

    private final KafkaTemplate<String, MarketPriceAvro> kafkaTemplate;
    private final Map<String, BigDecimal> lastPrices = new HashMap<>();

    private final ObjectMapper objectMapper;

    public MarketDataService(KafkaTemplate<String, MarketPriceAvro> kafkaTemplate,
                             MarketDataProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
        properties.getSymbols().forEach(s ->
                lastPrices.put(s, BigDecimal.valueOf(100 + new Random().nextInt(900)))
        );
    }

    public void publishPriceUpdates() {
        Random random = new Random();

        for (String symbol : lastPrices.keySet()) {
            BigDecimal currentPrice = lastPrices.get(symbol);
            BigDecimal delta = BigDecimal.valueOf((random.nextDouble() - 0.5) * 5);
            BigDecimal newPrice = currentPrice.add(delta).max(BigDecimal.ZERO).setScale(6, RoundingMode.HALF_UP);

            MarketPriceAvro marketPrice = buildMarketPriceAvro(symbol, newPrice);
            kafkaTemplate.send("market-data", marketPrice);

            log.info("Published new price: {}", marketPrice);
            lastPrices.put(symbol, newPrice);
        }
    }

    private MarketPriceAvro buildMarketPriceAvro(String symbol, BigDecimal newPrice) {
        return MarketPriceAvro.newBuilder()
                .setPrice(newPrice)
                .setSymbol(symbol)
                .setTimestamp(Instant.now().toEpochMilli())
                .build();
    }
}
