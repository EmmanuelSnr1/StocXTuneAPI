package com.stocxtune.api.service;

import com.stocxtune.api.dto.HoldingDTO;
import com.stocxtune.api.dto.PortfolioDTO;
import com.stocxtune.api.dto.TransactionDTO;
import com.stocxtune.api.model.Portfolio;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface PortfolioService {

    @Cacheable(value = "savePortfolio")
    PortfolioDTO save(PortfolioDTO portfolioDTO);

    @Cacheable(value = "findPortfolioByID")
    Optional<Portfolio> findById(Long id);

    List<PortfolioDTO> findAllByUserId(Long userId);



    @Cacheable(value = "updatePortfolio")
    PortfolioDTO updatePortfolio(Long id, PortfolioDTO portfolioDTO);

    @Cacheable(value = "deletePortfolio")
    void deletePortfolio(Long id);

    /**
     * Adds transactions to a specific portfolio.
     *
     * @param id The ID of the portfolio.
     * @param transactions The list of holdings to add.
     * @return The updated portfolio.
     */
    PortfolioDTO addTransactions(Long id, List<TransactionDTO> transactions);

    /**
     * Fetches all holdings for a specific portfolio.
     *
     * @return The list of holdings for the portfolio.
     */
    @Cacheable(value = "getHoldingsByPortfolioId")
    List<HoldingDTO> getHoldingsByPortfolioId(Long portfolioId);

    /**
     * Updates a specific holding in a portfolio.
     *
     * @param transactionId The ID of the portfolio.
     * @param transactionDTO The ID of the holding to update.
     * @return The updated portfolio.
     */
    TransactionDTO updateTransaction(Long transactionId, TransactionDTO transactionDTO);

    PortfolioDTO removeTransaction(Long portfolioId, Long transactionId);

    // Other CRUD operations...
}
