package com.andreas.markettradingservice.service;

import com.andreas.markettradingservice.model.MarketPriceData;
import com.andreas.markettradingservice.model.MarketPriceDataEntity;
import com.andreas.markettradingservice.repository.MarketPriceDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketDataServiceTest {

    @Mock
    private MarketCacheService cacheService;

    @Mock
    private MarketPriceDataRepository repository;

    @Mock
    private MarketPricePublisher publisher;

    @InjectMocks
    private MarketDataService marketDataService;

    private MarketPriceData sampleData;

    @BeforeEach
    void setUp() {
        sampleData = new MarketPriceData("BTC", new BigDecimal("12345.67"), Instant.now().toEpochMilli());
    }

    @Test
    void handleIncomingPrice_shouldSaveToCacheRepositoryAndPublish() {
        // Act
        marketDataService.handleIncomingPrice(sampleData);

        // Assert
        verify(cacheService, times(1)).saveMarketData(sampleData);
        verify(repository, times(1)).save(any(MarketPriceDataEntity.class));
        verify(publisher, times(1)).publish(sampleData);
    }

    @Test
    void handleIncomingPrice_shouldConvertToEntityCorrectly() {
        // Arrange
        ArgumentCaptor<MarketPriceDataEntity> entityCaptor = ArgumentCaptor.forClass(MarketPriceDataEntity.class);

        // Act
        marketDataService.handleIncomingPrice(sampleData);

        // Assert
        verify(repository).save(entityCaptor.capture());
        MarketPriceDataEntity entity = entityCaptor.getValue();

        assertEquals(sampleData.getSymbol(), entity.getSymbol());
        assertEquals(sampleData.getPrice(), entity.getPrice());
        assertEquals(sampleData.getTimestamp(), entity.getTimestamp());
    }
}
