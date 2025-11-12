package com.andreas.markettradingservice.controller;

import com.andreas.markettradingservice.model.MarketPriceData;
import com.andreas.markettradingservice.repository.MarketPriceDataRepository;
import com.andreas.markettradingservice.service.MarketCacheService;
import com.andreas.markettradingservice.service.MarketDataRetrievalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/market")
public class MarketPriceUserController {

    private final MarketDataRetrievalService retrievalService;

    public MarketPriceUserController(MarketDataRetrievalService retrievalService){
        this.retrievalService = retrievalService;
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<MarketPriceData> getMarketData(@PathVariable String symbol){
        return ResponseEntity.ok(retrievalService.getMarketData(symbol));
    }

}
