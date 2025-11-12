package com.andreas.markettradingservice.service;

import com.andreas.markettradingservice.model.MarketPriceData;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class MarketPricePublisher {

    private final Sinks.Many<MarketPriceData> sink;

    public MarketPricePublisher(){
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(MarketPriceData data){
        sink.tryEmitNext(data);
    }

    public Flux<MarketPriceData> getPriceStream(){
        return sink.asFlux();
    }
}
