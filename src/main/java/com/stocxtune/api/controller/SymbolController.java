package com.stocxtune.api.controller;

import com.stocxtune.api.service.YahooFinanceService;
import com.stocxtune.api.service.TwelveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/symbols")
public class SymbolController {

    private final YahooFinanceService yahooFinanceService;
    private final TwelveDataService twelveDataService;

    @Autowired
    public SymbolController(YahooFinanceService yahooFinanceService, TwelveDataService twelveDataService) {
        this.yahooFinanceService = yahooFinanceService;
        this.twelveDataService = twelveDataService;
    }

    @GetMapping("/search/{fragment}") // Passed
    public ResponseEntity<String> searchSymbols(@PathVariable String fragment) {
        String response = yahooFinanceService.searchSymbols(fragment);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{symbol}/profile")
    public ResponseEntity<String> fetchCompanyProfile(@PathVariable String symbol) {
        String response = yahooFinanceService.fetchCompanyProfile(symbol);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{symbol}/institution-ownership") // Working fine
    public ResponseEntity<String> fetchInstitutionOwnership(@PathVariable String symbol) {
        String response = yahooFinanceService.fetchInstitutionOwnership(symbol);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{symbol}/sec-filings") // Passed
    public ResponseEntity<String> fetchSECFilings(@PathVariable String symbol) {
        String response = yahooFinanceService.fetchSECFilings(symbol);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{symbol}/insider-holders") //Passed
    public ResponseEntity<String> fetchInsiderHoldings(@PathVariable String symbol) {
        String response = yahooFinanceService.fetchInsiderHoldings(symbol);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{symbol}/financial-data") //Passed
    public ResponseEntity<String> fetchKeyFinancials(@PathVariable String symbol) {
        String response = yahooFinanceService.fetchKeyFinancials(symbol);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{symbol}/key-stats") //Passed
    public ResponseEntity<String> fetchKeyStats(@PathVariable String symbol) {
        String response = yahooFinanceService.fetchKeyStats(symbol);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{symbol}/logo") //Passed
    public ResponseEntity<String> fetchCompanyLogo(@PathVariable String symbol) {
        String response = twelveDataService.fetchCompanyLogo(symbol);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{symbol}/fundamentals") //Passed
    public ResponseEntity<String> fetchCompanyFundamentals(@PathVariable String symbol) {
        String response = twelveDataService.fetchCompanyFundamentals(symbol);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{symbol}/time-series/{interval}")
    public ResponseEntity<String> fetchTimeSeriesData(@PathVariable String symbol, @PathVariable String interval) {
        String response = yahooFinanceService.fetchTimeSeriesData(symbol, interval);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/market-news")
    public ResponseEntity<String> fetchMarketNews() {
        String response = yahooFinanceService.fetchMarketNews();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/market-insider-trades")
    public ResponseEntity<String> fetchMarketInsiderTrades() {
        String response = yahooFinanceService.fetchMarketInsiderTrades();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{symbol}/news")
    public ResponseEntity<String> fetchStockNews(@PathVariable String symbol) {
        String response = yahooFinanceService.fetchStockNews(symbol);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/market-actives")
    public ResponseEntity<String> fetchMarketActives() {
        String response = yahooFinanceService.fetchMarketActives();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/day-losers")
    public ResponseEntity<String> fetchDayLosers() {
        String response = yahooFinanceService.fetchDayLosers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/day-gainers")
    public ResponseEntity<String> fetchDayGainers() {
        String response = yahooFinanceService.fetchDayGainers();
        return ResponseEntity.ok(response);
    }
}
