package com.stocxtune.api.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

@Service
public class TwelveDataService {

    private static final String API_KEY = "d75e476636msha08ea07fdb84f37p11a6ccjsnf6345c38ebe8";
    private static final String API_HOST = "twelve-data1.p.rapidapi.com";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    public String fetchStockData(String url) {
        HttpRequest request = buildRequest(url);
        return executeRequest(request);
    }

    public String fetchCompanyLogo(String symbol) {
        String url = "https://twelve-data1.p.rapidapi.com/logo?symbol=" + symbol;
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
}
