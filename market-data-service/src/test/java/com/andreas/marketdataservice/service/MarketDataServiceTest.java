package com.andreas.marketdataservice.service;

import com.andreas.common.avro.MarketPriceAvro;
import com.andreas.marketdataservice.config.MarketDataProperties;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class MarketDataServiceTest {

    @Mock
    private KafkaTemplate<String, MarketPriceAvro> kafkaTemplate;

    private MarketDataService marketDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MarketDataProperties properties = new MarketDataProperties();
        properties.setSymbols(List.of("BTC", "ETH"));

        CompletableFuture<SendResult<String, MarketPriceAvro>> future =
                CompletableFuture.completedFuture(new SendResult<>(null, (RecordMetadata) null));
        when(kafkaTemplate.send(any(String.class), any(MarketPriceAvro.class))).thenReturn(future);

        marketDataService = new MarketDataService(kafkaTemplate, properties);
    }

    @Test
    void shouldInitializePricesForConfiguredSymbols() {
        Map<String, BigDecimal> prices = getLastPrices();
        assertThat(prices).containsKeys("BTC", "ETH");
        assertThat(prices.get("BTC")).isNotNull();
        assertThat(prices.get("ETH")).isNotNull();
    }

    @Test
    void shouldPublishPriceUpdatesForAllSymbols() {
        marketDataService.publishPriceUpdates();

        ArgumentCaptor<MarketPriceAvro> captor = ArgumentCaptor.forClass(MarketPriceAvro.class);
        verify(kafkaTemplate, times(2)).send(eq("market-data"), captor.capture());

        List<MarketPriceAvro> published = captor.getAllValues();
        assertThat(published).hasSize(2);
        assertThat(published)
                .allSatisfy(p -> {
                    assertThat(p.getSymbol()).isIn("BTC", "ETH");
                    assertThat(p.getPrice()).isNotNull();
                    assertThat(p.getTimestamp()).isPositive();
                });
    }

    @Test
    void shouldUpdatePricesAfterPublishing() {
        Map<String, BigDecimal> before = getLastPrices();
        BigDecimal btcBefore = before.get("BTC");

        marketDataService.publishPriceUpdates();

        Map<String, BigDecimal> after = getLastPrices();
        BigDecimal btcAfter = after.get("BTC");

        assertThat(btcAfter).isNotNull();
        assertThat(btcAfter).isNotEqualTo(btcBefore);
        assertThat(btcAfter).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @SuppressWarnings("unchecked")
    private Map<String, BigDecimal> getLastPrices() {
        try {
            var field = MarketDataService.class.getDeclaredField("lastPrices");
            field.setAccessible(true);
            return (Map<String, BigDecimal>) field.get(marketDataService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
