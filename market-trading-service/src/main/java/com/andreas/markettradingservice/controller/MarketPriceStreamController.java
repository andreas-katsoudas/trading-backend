package com.andreas.markettradingservice.controller;

import com.andreas.markettradingservice.model.MarketPriceData;
import com.andreas.markettradingservice.service.MarketPricePublisher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/market/stream")
public class MarketPriceStreamController {

    private final MarketPricePublisher publisher;

    public MarketPriceStreamController(MarketPricePublisher publisher) {
        this.publisher = publisher;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MarketPriceData> streamPrices() {
        return publisher.getPriceStream();
    }
}

