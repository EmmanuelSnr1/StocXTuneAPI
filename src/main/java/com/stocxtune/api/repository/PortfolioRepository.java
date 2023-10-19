package com.stocxtune.api.repository;

import com.stocxtune.api.model.Portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

    // Custom query to find all portfolios associated with a particular user.
    List<Portfolio> findByUserId(Long userId);

    List<Portfolio> findByUser_Email(String email);

    // Custom query to find a portfolio by its name.
    Portfolio findByName(String name);

    // Other custom query methods for Portfolio can be added here if needed.
}

