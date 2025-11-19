package com.andreas.markettradingservice.service;

import com.andreas.markettradingservice.model.MarketPriceData;
import com.andreas.markettradingservice.repository.MarketPriceDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MarketDataRetrievalService {

    private final MarketCacheService cacheService;
    private final MarketPriceDataRepository repository;

    public MarketDataRetrievalService(MarketCacheService cacheService, MarketPriceDataRepository repository){
        this.cacheService = cacheService;
        this.repository = repository;
    }

    public MarketPriceData getMarketData(String symbol){
        try {
            MarketPriceData data = cacheService.getMarketData(symbol);

            if(data != null) return data;
        } catch (Exception e){
            log.warn("Redis unavailable, fetching from DB", e);
        }
        return  repository.findTopBySymbolOrderByTimestampDesc(symbol)
                .map(entity -> new MarketPriceData(entity.getSymbol(), entity.getPrice(), entity.getTimestamp()))
                .orElse(null);
    }

}
