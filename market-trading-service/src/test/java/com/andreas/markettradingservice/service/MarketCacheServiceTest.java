package com.andreas.markettradingservice.service;

import com.andreas.markettradingservice.model.MarketPriceData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MarketCacheServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private ValueOperations<String, Object> valueOperations;
    @InjectMocks
    private MarketCacheService service;


    @Test
    void saveMarketDataTest(){
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        MarketPriceData data = new MarketPriceData();
        data.setSymbol("BTC");
        data.setPrice(BigDecimal.valueOf(1.992));
        data.setTimestamp(Instant.now().toEpochMilli());

        service.saveMarketData(data);

        verify(valueOperations).set(eq("BTC"),eq(data), eq(Duration.ofMinutes(10)));
    }

    @Test
    void getMarketDataTest(){
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(service.getMarketData("BTC")).thenReturn(buildDataMock());

        MarketPriceData data = service.getMarketData("BTC");

        assertEquals("BTC", data.getSymbol());
        assertEquals(BigDecimal.valueOf(1.992), data.getPrice());
    }

    @Test
    void deleteMarketDataTest(){
        service.deleteMarketData("BTC");

        verify(redisTemplate, times(1)).delete("BTC");
    }

    private MarketPriceData buildDataMock(){
        return new MarketPriceData("BTC", BigDecimal.valueOf(1.992), Instant.now().toEpochMilli());
    }

}
