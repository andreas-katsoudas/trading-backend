package com.andreas.markettradingservice.service;

import com.andreas.markettradingservice.model.MarketPriceData;
import com.andreas.markettradingservice.model.MarketPriceDataEntity;
import com.andreas.markettradingservice.repository.MarketPriceDataRepository;
import org.springframework.stereotype.Service;

@Service
public class MarketHandleDataService {

    private final MarketCacheService cacheService;
    private final MarketPriceDataRepository repository;
    private final MarketPricePublisher publisher;

    public MarketHandleDataService(MarketCacheService cacheService,
                                   MarketPricePublisher publisher,
                                   MarketPriceDataRepository repository){
        this.cacheService = cacheService;
        this.repository = repository;
        this.publisher = publisher;
    }

    public void handleIncomingPrice(MarketPriceData data){
        cacheService.saveMarketData(data);
        repository.save(convertDataToEntity(data));
        publisher.publish(data);
    }

    private MarketPriceDataEntity convertDataToEntity(MarketPriceData data){
        return MarketPriceDataEntity
                .builder()
                .price(data.getPrice())
                .timestamp(data.getTimestamp())
                .symbol(data.getSymbol())
                .build();

    }

}
