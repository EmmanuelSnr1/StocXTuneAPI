package com.stocxtune.api.repository;

import com.stocxtune.api.model.stock.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // Custom query to find a stock by its symbol.
    Stock findBySymbol(String symbol);

    // Custom query to find stocks by their name (could return multiple stocks with similar names).
    List<Stock> findByNameContaining(String name);

    // Other custom query methods for Stock can be added here if needed.
}
