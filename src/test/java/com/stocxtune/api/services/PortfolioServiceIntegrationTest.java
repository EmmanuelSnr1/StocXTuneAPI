package com.stocxtune.api.services;

import com.stocxtune.api.dto.HoldingDTO;
import com.stocxtune.api.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertNotNull;

@SpringBootTest
@Transactional // To rollback DB changes after the test
public class PortfolioServiceIntegrationTest {

    @Autowired
    private PortfolioService portfolioService;

    @Test
     public void testGetHoldingsByPortfolioId() {
        // Given
        Long portfolioId = 6L; // Make sure this ID exists in your test database

        // When
        List<HoldingDTO> holdings = portfolioService.getHoldingsByPortfolioId(portfolioId);

        // Then
        assertNotNull("Holdings should not be null", holdings);
        // Add more assertions here to validate the actual data retrieved from the database
    }
}

