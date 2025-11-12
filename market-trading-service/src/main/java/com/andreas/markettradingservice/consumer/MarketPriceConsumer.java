package com.andreas.markettradingservice.consumer;

import com.andreas.common.avro.MarketPriceAvro;
import com.andreas.markettradingservice.model.MarketPriceData;
import com.andreas.markettradingservice.model.MarketPriceDataEntity;
import com.andreas.markettradingservice.repository.MarketPriceDataRepository;
import com.andreas.markettradingservice.service.MarketCacheService;
import com.andreas.markettradingservice.service.MarketDataService;
import com.andreas.markettradingservice.service.MarketPricePublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MarketPriceConsumer {

    private final MarketDataService service;

    public MarketPriceConsumer(MarketDataService service) {
        this.service = service;
    }
    @KafkaListener(topics = "market-data", groupId = "trading-service-group")
    public void consume(MarketPriceAvro marketPrice) {
        log.info("Received: " + marketPrice);
        MarketPriceData data = convertAvroToData(marketPrice);
        try{
            service.handleIncomingPrice(data);
        }catch (Exception e){
            log.error("Failed processing message: " + marketPrice, e);
        }

    }


    private MarketPriceData convertAvroToData(MarketPriceAvro avro){
        MarketPriceData data = new MarketPriceData();
        data.setPrice(avro.getPrice());
        data.setSymbol(avro.getSymbol());
        data.setTimestamp(avro.getTimestamp());

        return data;
    }

}
