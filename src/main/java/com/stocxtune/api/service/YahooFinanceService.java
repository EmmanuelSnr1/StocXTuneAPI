package com.stocxtune.api.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

@Service
public class YahooFinanceService {

    private static final String YAHOO_BASE_URL = "https://yahoo-finance15.p.rapidapi.com/api/yahoo/";
    private static final String API_KEY = "03e1e97663msh35f5cc76e6b5ce5p11427fjsn835e3d542999";

    @Cacheable(value = "symbols")
    public String searchSymbols(String fragment) {
        return fetchData(YAHOO_BASE_URL + "sc/search/" + fragment);
    }

    @Cacheable(value = "companyProfiles")
    public String fetchCompanyProfile(String symbol) {
        return fetchData(YAHOO_BASE_URL + "qu/quote/" + symbol + "/asset-profile");
    }

    @Cacheable(value = "institutionOwnerships")
    public String fetchInstitutionOwnership(String symbol) {
        return fetchData(YAHOO_BASE_URL + "qu/quote/" + symbol + "/institution-ownership");
    }

    @Cacheable(value = "secFilings")
    public String fetchSECFilings(String symbol) {
        return fetchData(YAHOO_BASE_URL + "qu/quote/" + symbol + "/sec-filings");
    }
    @Cacheable(value = "insiderHoldings")
    public String fetchInsiderHoldings(String symbol) {
        return fetchData(YAHOO_BASE_URL + "qu/quote/" + symbol + "/insider-holders");
    }

    @Cacheable(value = "keyFinancials")
    public String fetchKeyFinancials(String symbol) {
        return fetchData(YAHOO_BASE_URL + "qu/quote/" + symbol + "/financial-data");
    }
    @Cacheable(value = "keyStats")
    public String fetchKeyStats(String symbol) {
        return fetchData(YAHOO_BASE_URL + "qu/quote/" + symbol + "/default-key-statistics");
    }
    @Cacheable(value = "stockNews")
    public String fetchStockNews(String symbol) {
        String url = YAHOO_BASE_URL + "ne/news/" + symbol;
        return fetchData(url);
    }

    @Cacheable(value = "marketNews")
    public String fetchMarketNews() {
        String url = YAHOO_BASE_URL + "ne/news";
        return fetchData(url);
    }

    @Cacheable(value = "marketInsiderTrades")
    public String fetchMarketInsiderTrades() {
        String url = YAHOO_BASE_URL + "v1/sec/form4";
        return fetchData(url);
    }

    @Cacheable(value = "timeSeriesData")
    public String fetchTimeSeriesData(String symbol, String interval) {
        validateInterval(interval);
        return fetchData(YAHOO_BASE_URL + "hi/history/" + symbol + "/" + interval + "?diffandsplits=false");
    }


    @Cacheable(value = "marketActives")
    public String fetchMarketActives() {
        return fetchData(YAHOO_BASE_URL + "co/collections/most_actives");
    }

    @Cacheable(value = "marketLosers")
    public String fetchDayLosers() {
        return fetchData(YAHOO_BASE_URL + "co/collections/day_losers");
    }

    @Cacheable(value = "marketGainers")
    public String fetchDayGainers() {
        return fetchData(YAHOO_BASE_URL + "co/collections/day_gainers");
    }

    private void validateInterval(String interval) {
        String[] validIntervals = {"5m", "15m", "30m", "1h", "1d", "1wk", "1mo", "3mo"};
        boolean isValid = false;
        for (String validInterval : validIntervals) {
            if (interval.equals(validInterval)) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new IllegalArgumentException("Invalid interval provided: " + interval);
        }
    }

    private String fetchData(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", "yahoo-finance15.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching data from Yahoo Finance API.";
        }
    }
}
