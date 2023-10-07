package com.stocxtune.api.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TwelveDataService {

    private static final String API_KEY = "03e1e97663msh35f5cc76e6b5ce5p11427fjsn835e3d542999";
    private static final String API_HOST = "twelve-data1.p.rapidapi.com";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    @Cacheable(value = "companyLogos")
    public String fetchCompanyLogo(String symbol) {
        return fetchData("https://twelve-data1.p.rapidapi.com/logo?symbol=" + symbol);
    }

    @Cacheable(value = "companyFundamentals")
    public String fetchCompanyFundamentals(String symbol) {
        String url = "https://twelve-data1.p.rapidapi.com/quote?symbol=" + symbol + "&interval=1day&outputsize=30&format=json";
        return fetchData(url);
    }


    private String fetchData(String url) {
        HttpRequest request = buildRequest(url);
        return executeRequest(request);
    }

    private HttpRequest buildRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", API_HOST)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
    }

    private String executeRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching data from Twelve Data API.";
        }
    }

    // Cache Settings.
    @CacheEvict(value = "companyLogos")
    public void clearCachedLogo(String symbol) {
        // This method will clear the cached logo for the given symbol
    }
}
