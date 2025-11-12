package com.andreas.marketdataservice.scheduler;

import com.andreas.marketdataservice.service.MarketDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarketDataScheduler {

    private final MarketDataService marketDataService;


    @Scheduled(fixedRateString = "${marketdata.update-interval-ms}")
    public void updatePrices() {
        marketDataService.publishPriceUpdates();
    }
}
