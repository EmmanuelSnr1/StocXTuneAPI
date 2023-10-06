package com.stocxtune.api.services;

import com.stocxtune.api.dto.SymbolDTO;
import com.stocxtune.api.service.YahooFinanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SpringBootTest
public class YahooFinanceServiceTest {

    private YahooFinanceService yahooFinanceService;

    private static final Logger logger = LoggerFactory.getLogger(YahooFinanceServiceTest.class);

    @BeforeEach
    public void setUp() {
        yahooFinanceService = new YahooFinanceService();
    }

//    @Test
//    public void testSearchSymbols() {
//        List<SymbolDTO> symbols = yahooFinanceService.searchSymbols("AA"); // or any other fragment you want to test with
//        symbols.forEach(symbol -> logger.info(symbol.toString()));
//    }
}
