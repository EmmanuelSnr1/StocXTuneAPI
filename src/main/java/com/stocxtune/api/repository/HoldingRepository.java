package com.stocxtune.api.repository;

import com.stocxtune.api.model.Holding;
import com.stocxtune.api.model.Portfolio;
import com.stocxtune.api.model.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {

    // Custom query to find all holdings associated with a particular portfolio.
    List<Holding> findByPortfolioId(Long portfolioId);

    List<Holding> findByPortfolioIdAndStockId(Long portfolioId, Long stockId);

    Optional<Holding> findByPortfolioAndStock(Portfolio portfolio, Stock stock);



    // Other custom query methods for Holding can be added here if needed.
}
