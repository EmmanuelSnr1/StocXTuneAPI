package com.stocxtune.api.repository;

import com.stocxtune.api.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {

    // Custom query to find all holdings associated with a particular portfolio.
    List<Holding> findByPortfolioId(Long portfolioId);

    List<Holding> findByPortfolioIdAndStockId(Long portfolioId, Long stockId);


    // Other custom query methods for Holding can be added here if needed.
}
