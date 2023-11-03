package com.stocxtune.api.repository;

import com.stocxtune.api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Custom query to find all transactions associated with a particular portfolio.
    @Query("SELECT t FROM Transaction t WHERE t.portfolio.id = :portfolioId")
    List<Transaction> findByPortfolioId(Long portfolioId);

    // Other custom query methods for Transaction can be added here if needed.
}
