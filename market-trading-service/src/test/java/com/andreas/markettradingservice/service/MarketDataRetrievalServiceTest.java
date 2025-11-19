package com.andreas.markettradingservice.service;

import com.andreas.markettradingservice.model.MarketPriceData;
import com.andreas.markettradingservice.repository.MarketPriceDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.andreas.markettradingservice.util.MarketDataTestsUtil.buildDataMock;
import static com.andreas.markettradingservice.util.MarketDataTestsUtil.buildDataMockEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarketDataRetrievalServiceTest {

    @Mock
    private MarketCacheService cacheService;
    @Mock
    private MarketPriceDataRepository repository;

    @InjectMocks
    private MarketDataRetrievalService service;

    @Test
    void retrieveMarketDataFromRedisTest(){
        when(cacheService.getMarketData("BTC")).thenReturn(buildDataMock());

        MarketPriceData data = service.getMarketData("BTC");

        assertEquals("BTC", data.getSymbol());
        assertEquals(BigDecimal.valueOf(1.992), data.getPrice());
    }

    @Test
    void retrieveMarketDataFromRepositoryTest(){
        when(cacheService.getMarketData(any())).thenReturn(null);
        when(repository.findTopBySymbolOrderByTimestampDesc("BTC")).thenReturn(Optional.of(buildDataMockEntity()));

        MarketPriceData data = service.getMarketData("BTC");
        assertEquals("BTC", data.getSymbol());
        assertEquals(BigDecimal.valueOf(1.992), data.getPrice());
    }

}
