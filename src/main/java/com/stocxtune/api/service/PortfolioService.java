package com.stocxtune.api.service;

import com.stocxtune.api.dto.HoldingDTO;
import com.stocxtune.api.dto.PortfolioDTO;
import com.stocxtune.api.dto.TransactionDTO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface PortfolioService {

    @Cacheable(value = "savePortfolio")
    PortfolioDTO save(PortfolioDTO portfolioDTO);

    @Cacheable(value = "findPortfolioByID")
    PortfolioDTO findById(Long id);

    @Cacheable(value = "findAllPortfolios")
    List<PortfolioDTO> findAll();

    List<PortfolioDTO> findAllByUserId(Long userId);

    @Cacheable(value = "getPortfolioByUserEmail")
    List<PortfolioDTO> getPortfolioByUserEmail(String email);

    @Cacheable(value = "updatePortfolio")
    PortfolioDTO update(Long id, PortfolioDTO portfolioDTO);

    @Cacheable(value = "deletePortfolio")
    void deleteById(Long id);

    PortfolioDTO updateDetails(Long id, PortfolioDTO portfolioDTO);

    PortfolioDTO addTransactions(Long id, List<TransactionDTO> transactions);

    PortfolioDTO removeTransactions(Long id, List<Long> transactionIds);

    /**
     * Adds holdings to a specific portfolio.
     *
     * @param id The ID of the portfolio.
     * @param holdings The list of holdings to add.
     * @return The updated portfolio.
     */
    PortfolioDTO addHoldings(Long id, List<HoldingDTO> holdings);

    /**
     * Removes holdings from a specific portfolio.
     *
     * @param id The ID of the portfolio.
     * @param holdingIds The list of holding IDs to remove.
     * @return The updated portfolio.
     */
    PortfolioDTO removeHoldings(Long id, List<Long> holdingIds);

    /**
     * Updates a specific holding in a portfolio.
     *
     * @param portfolioId The ID of the portfolio.
     * @param holdingId The ID of the holding to update.
     * @param holdingDTO The updated holding data.
     * @return The updated portfolio.
     */
    PortfolioDTO updateHolding(Long portfolioId, Long holdingId, HoldingDTO holdingDTO);

    /**
     * Fetches all holdings for a specific portfolio.
     *
     * @param id The ID of the portfolio.
     * @return The list of holdings for the portfolio.
     */
    List<HoldingDTO> getHoldingsByPortfolioId(Long portfolioId);

    PortfolioDTO removeHolding(Long portfolioId, Long holdingId);

    PortfolioDTO removeTransaction(Long portfolioId, Long transactionId);

    // Other CRUD operations...
}
