package com.stocxtune.api.repository;

import com.stocxtune.api.config.StockApiConfig;
import com.stocxtune.api.exception.ApiException;
import com.stocxtune.api.model.stock.Asset;
import com.stocxtune.api.model.stock.StockSearchResult;
import com.stocxtune.api.service.MyJsonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class StockDataRepo {

    private final static String baseUrl = "https://www.alphavantage.co/query";

    StockApiConfig stockApiConfig;
    RestTemplate restTemplate;
    MyJsonMapper myJsonMapper;

    @Autowired
    public StockDataRepo(StockApiConfig stockApiConfig, MyJsonMapper myJsonMapper) {

        this.stockApiConfig = stockApiConfig;
        this.myJsonMapper = myJsonMapper;
        this.restTemplate = new RestTemplate();

    }

    private ResponseEntity<JsonNode> getResponseForQuote(String symbol) {

        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                .encode()
                .queryParam("function", "{function}")
                .queryParam("symbol", "{symbol}")
                .queryParam("apikey", "{API_KEY}")
                .toUriString();

        Map<String, String> params = new HashMap<>();

        params.put("function", "GLOBAL_QUOTE");
        params.put("symbol", symbol);
        params.put("API_KEY", this.stockApiConfig.getAPI_KEY());
        System.out.println("Final uri: " + uri);

        return restTemplate.getForEntity(uri, JsonNode.class, params);
    }


    private ResponseEntity<JsonNode> getResponseForSearching(String keywords) {
        String uri = UriComponentsBuilder.fromUriString(baseUrl)
                .encode()
                .queryParam("function", "{function}")
                .queryParam("keywords", "{keywords}")
                .queryParam("apikey", "{API_KEY}")
                .toUriString();

        Map<String, String> params = new HashMap<>();

        params.put("function", "SYMBOL_SEARCH");
        params.put("keywords", keywords);
        params.put("API_KEY", this.stockApiConfig.getAPI_KEY());
        System.out.println("Final uri: " + uri);

        return restTemplate.getForEntity(uri, JsonNode.class, params);
    }

    // Updates the details of the asset... i.e. updates the LTP, open, low, high, close, etc.
    public void updateStock(Asset asset) throws ApiException {

        ResponseEntity<JsonNode> responseEntity = getResponseForQuote(asset.getSymbol());

        System.out.println("Response: " + responseEntity);

        if (responseEntity.getStatusCodeValue() / 100 == 2) {

            printResponse(responseEntity);

            JsonNode obj = Objects.requireNonNull(responseEntity.getBody()).get("Global Quote");

            try {
                Asset result = this.myJsonMapper.getStockFromNode(obj);

                System.out.println("Asset: asset");

                asset.setLTP(result.getLTP());
                asset.setHigh(result.getHigh());
                asset.setLow(result.getLow());
                asset.setPreviousClose(result.getPreviousClose());
                asset.setPreviousOpen(result.getPreviousOpen());

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else
            throw new ApiException(String.valueOf(responseEntity.getStatusCodeValue()));

    }

    // Converts a Asset SearchResult to a Asset.
    public Asset getStockFromStockSearchResult(StockSearchResult stockSearchResult) throws ApiException {
        Asset asset = new Asset(stockSearchResult.getSymbol(), stockSearchResult.getName());
        updateStock(asset);
        return asset;
    }

    // Return a List of intermediate Asset objects that match the query
    public List<StockSearchResult> searchForSymbol(String query) throws ApiException {

        ResponseEntity<JsonNode> responseEntity = getResponseForSearching(query.strip());

        System.out.println("Response: " + responseEntity);

        if (responseEntity.getStatusCodeValue() / 100 == 2) {

            printResponse(responseEntity);

            JsonNode oj = Objects.requireNonNull(responseEntity.getBody()).get("bestMatches");

            try {
                List<StockSearchResult> result = this.myJsonMapper.getAllFromNode(oj);
                result = result.stream().filter(ssr -> ssr.getRegion().contains("India")).collect(Collectors.toList());
                System.out.println("result: " + result);

                System.out.println("Returning list: " + result);
                return result;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }

        throw new ApiException(String.valueOf(responseEntity.getStatusCodeValue()));

    }

    // Get a stock from a symbol.
    public Asset getStockFromSymbol(String symbol) throws ApiException, IllegalArgumentException {

        ResponseEntity<JsonNode> responseEntity = getResponseForSearching(symbol.split("\\.")[0]);

        System.out.println("Response: " + responseEntity);

        if (responseEntity.getStatusCodeValue() / 100 == 2) {

            printResponse(responseEntity);

            JsonNode oj = Objects.requireNonNull(responseEntity.getBody()).get("bestMatches");

            try {
                List<StockSearchResult> result = this.myJsonMapper.getAllFromNode(oj);

                if (result.size() == 0)
                    throw new IllegalArgumentException();

                StockSearchResult bestMatch = result.get(0);
                double bestScore = bestMatch.getMatchScore();

                for (StockSearchResult ssr : result) {
                    if (ssr.getMatchScore() >= bestScore) {
                        bestMatch = ssr;
                        bestScore = ssr.getMatchScore();
                    }
                }

                return getStockFromStockSearchResult(bestMatch);


            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        throw new ApiException(String.valueOf(responseEntity.getStatusCodeValue()));
    }

    // Helper method to print a Response from the API.
    public static void printResponse(ResponseEntity<?> responseEntity) {

        System.out.println("--------------Status--------------");
        System.out.println(responseEntity.getStatusCode());
        System.out.println("--------------Headers--------------");
        System.out.println(responseEntity.getHeaders());
        System.out.println("--------------Body--------------");
        System.out.println(responseEntity.getBody());
    }


}
