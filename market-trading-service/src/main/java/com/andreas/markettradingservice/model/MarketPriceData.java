package com.andreas.markettradingservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketPriceData implements Serializable {

    private String symbol;
    private BigDecimal price;
    private Long timestamp;

}