package com.andreas.markettradingservice.util;

import com.andreas.markettradingservice.model.MarketPriceData;
import com.andreas.markettradingservice.model.MarketPriceDataEntity;
import org.testcontainers.shaded.org.yaml.snakeyaml.error.Mark;

import java.math.BigDecimal;
import java.time.Instant;

public class MarketDataTestsUtil {

    public static MarketPriceData buildDataMock(){
        return new MarketPriceData("BTC", BigDecimal.valueOf(1.992), Instant.now().toEpochMilli());
    }

    public static MarketPriceDataEntity buildDataMockEntity(){
        return new MarketPriceDataEntity(1L, "BTC", BigDecimal.valueOf(1.992), Instant.now().toEpochMilli());
    }

}
