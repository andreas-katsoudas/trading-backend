package com.andreas.markettradingservice.service;

import com.andreas.markettradingservice.model.MarketPriceData;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class MarketCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public MarketCacheService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void saveMarketData(MarketPriceData data){
        redisTemplate.opsForValue().set(data.getSymbol(), data, Duration.ofMinutes(10));
    }

    public MarketPriceData getMarketData(String symbol){
        return (MarketPriceData) redisTemplate.opsForValue().get(symbol);
    }

    public void deleteMarketData(String symbol){
        redisTemplate.delete(symbol);
    }
}
