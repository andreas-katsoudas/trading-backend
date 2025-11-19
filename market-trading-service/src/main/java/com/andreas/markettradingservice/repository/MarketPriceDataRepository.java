package com.andreas.markettradingservice.repository;

import com.andreas.markettradingservice.model.MarketPriceDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketPriceDataRepository extends JpaRepository<MarketPriceDataEntity, Long> {

    Optional<MarketPriceDataEntity> findTopBySymbolOrderByTimestampDesc(String symbol);

}
