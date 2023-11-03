package com.stocxtune.api.services;

import com.stocxtune.api.dao.UserDao;
import com.stocxtune.api.dto.PortfolioDTO;
import com.stocxtune.api.model.Portfolio;
import com.stocxtune.api.model.User;
import com.stocxtune.api.repository.PortfolioRepository;
import com.stocxtune.api.service.impl.PortfolioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PortfolioServiceImplTest {

    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private UserDao userDao;

    // Initialize mocks
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        // Given
        PortfolioDTO portfolioDTO = new PortfolioDTO();
        portfolioDTO.setName("Test Portfolio");
        portfolioDTO.setDescription("Test Description");
//        portfolioDTO.setUser("test@example.com");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        Portfolio mockPortfolio = new Portfolio();
        mockPortfolio.setName(portfolioDTO.getName());
        mockPortfolio.setDescription(portfolioDTO.getDescription());
        mockPortfolio.setUser(mockUser);

        when(userDao.getUserByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(portfolioRepository.save(any(Portfolio.class))).thenReturn(mockPortfolio);

        // When
        PortfolioDTO savedPortfolioDTO = portfolioService.save(portfolioDTO);

        // Then
        assertNotNull(savedPortfolioDTO);
        assertEquals("Test Portfolio", savedPortfolioDTO.getName());
        verify(portfolioRepository, times(1)).save(any(Portfolio.class));
    }

}
