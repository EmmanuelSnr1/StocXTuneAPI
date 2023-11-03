package com.stocxtune.api.service.impl;

import com.stocxtune.api.dao.UserDao;
import com.stocxtune.api.dto.StockDTO;
import com.stocxtune.api.dto.WatchlistDTO;
import com.stocxtune.api.model.User;
import com.stocxtune.api.model.stock.Stock;
import com.stocxtune.api.repository.WatchlistRepository;
import com.stocxtune.api.model.Watchlist;
import com.stocxtune.api.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.stocxtune.api.service.YahooFinanceService;
import com.stocxtune.api.service.TwelveDataService;




//If you take a look at an entity above you can see they reflect a model for our domain or business logic in the application.
//A DTO on the other hand will not be responsible for such things, they will only reflect what the consumer requesting the data needs.
@Service
public class WatchlistServiceImpl implements WatchlistService {

    @Autowired
    private YahooFinanceService yahooFinanceService;

    @Autowired
    private TwelveDataService twelveDataService ;



    @Autowired
    private WatchlistRepository watchlistRepository;

    private static final Logger logger = LoggerFactory.getLogger(WatchlistServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public WatchlistDTO save(WatchlistDTO watchlistDTO) {
        Watchlist watchlist = new Watchlist();

        // Convert watchlistDTO to watchlist entity and set attributes
        watchlist.setName(watchlistDTO.getName());
        watchlist.setDescription(watchlistDTO.getDescription());

        // Using UserDao to fetch the User entity using the email
        User user = userDao.getUserByEmail(watchlistDTO.getUser())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + watchlistDTO.getUser()));
        watchlist.setUser(user);
        // Convert StockDTOs to Stock entities
        if (watchlistDTO.getStocks() != null && !watchlistDTO.getStocks().isEmpty()) {
            List<Stock> stocks = watchlistDTO.getStocks().stream()
                    .map(stockDTO -> {
                        Stock stock = new Stock();
                        stock.setSymbol(stockDTO.getSymbol());
                        stock.setName(stockDTO.getName());
                        // Set other stock attributes from stockDTO here...
                        return stock;
                    })
                    .collect(Collectors.toList());
            watchlist.setStocks(stocks);
        }

        watchlist = watchlistRepository.save(watchlist);
        logger.info("Saved watchlist: {}", watchlist);
        return convertToDTO(watchlist);
    }


    // returns all the watchlists in the database.
    @Override
    public WatchlistDTO findById(Long id) {
        Optional<Watchlist> watchlist = watchlistRepository.findById(id);
        return watchlist.map(this::convertToDTO).orElse(null);
    }

    public List<WatchlistDTO> getWatchlistByUserEmail(String email) {
        List<Watchlist> watchlist = watchlistRepository.findByUser_Email(email);
        // Convert the list of Watchlist entities to WatchlistDTOs and return
        return watchlist.stream()
                .map(this::convertToDTO)  // Assuming you have a method called convertToDTO
                .collect(Collectors.toList());
    }

    @Override
    public List<WatchlistDTO> findAll() {
        return watchlistRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<WatchlistDTO> findAllByUserId(Long userId) {
        List<Watchlist> watchlist = watchlistRepository.findByUserId(userId);

        return watchlist.stream()
                .map(this::convertToDTO)  // Assuming you have a method called convertToDTO
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        watchlistRepository.deleteById(id);
    }

    private WatchlistDTO convertToDTO(Watchlist watchlist) {
        WatchlistDTO dto = new WatchlistDTO();

        // Convert watchlist entity to watchlistDTO and set attributes
        dto.setId(watchlist.getId());
        dto.setName(watchlist.getName());
        dto.setDescription(watchlist.getDescription());

        // Set the userEmail from the User entity associated with the watchlist
        if (watchlist.getUser() != null) {
            dto.setUser(watchlist.getUser().getEmail()); // Updated this line
        }

        // Convert Stock entities to StockDTOs
        if (watchlist.getStocks() != null && !watchlist.getStocks().isEmpty()) {
            List<StockDTO> stockDTOs = watchlist.getStocks().stream()
                    .map(stock -> {
                        StockDTO stockDTO = new StockDTO();
                        stockDTO.setId(stock.getId());
                        stockDTO.setSymbol(stock.getSymbol());
                        stockDTO.setName(stock.getName());
                        // Set other attributes from stock to stockDTO here...
                        // Fetch key financials for the stock
                        String financialDataJson = twelveDataService.fetchCompanyFundamentals(stock.getSymbol());
                        try {
                            JSONObject financialData = new JSONObject(financialDataJson);

                            // Extracting current price
                            if (financialData.has("close")) {
                                stockDTO.setCurrentPrice(financialData.getDouble("close"));
                            }

                            // Extracting percentage change
                            if (financialData.has("percent_change")) {
                                stockDTO.setPercentageChange(financialData.getDouble("percent_change"));
                            }

                            // You can add other attributes extraction here as needed...

                        } catch (JSONException e) {
                            // Log the error or handle it as appropriate
                            System.err.println("Error parsing JSON for stock: " + stock.getSymbol());
                            e.printStackTrace();
                        }

//                        logger.info(" Converted Watchlist : {}", stockDTO);

                        return stockDTO;


                    })
                    .collect(Collectors.toList());
            dto.setStocks(stockDTOs);
        }

        return dto;
    }

    public WatchlistDTO updateDetails(Long id, WatchlistDTO watchlistDTO) {
        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist not found with ID: " + id));

        if (watchlistDTO.getName() != null) {
            watchlist.setName(watchlistDTO.getName());
        }

        if (watchlistDTO.getDescription() != null) {
            watchlist.setDescription(watchlistDTO.getDescription());
        }

        watchlistRepository.save(watchlist);
        return convertToDTO(watchlist);
    }

    public WatchlistDTO addStocks(Long id, List<StockDTO> stocks) {
        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist not found with ID: " + id));

        List<Stock> newStocks = convertDTOsToStocks(stocks);
        watchlist.getStocks().addAll(newStocks);

         watchlistRepository.save(watchlist);
        return convertToDTO(watchlist);
    }

    public WatchlistDTO removeStocks(Long id, List<Long> stockIds) {
        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist not found with ID: " + id));

        watchlist.getStocks().removeIf(stock -> stockIds.contains(stock.getId()));

        watchlistRepository.save(watchlist);
        return convertToDTO(watchlist);
    }

//    public WatchlistDTO updateStocks(Long id, List<StockDTO> stocks) {
//        // Implement the logic to update stock symbols
//        // Similar to the logic you provided in the initial update method
//    }


    public WatchlistDTO update(Long id, WatchlistDTO watchlistDTO) {
        Watchlist watchlist = watchlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Watchlist not found with ID: " + id));

        // Update name if provided
        if (watchlistDTO.getName() != null) {
            watchlist.setName(watchlistDTO.getName());
        }

        // Update description if provided
        if (watchlistDTO.getDescription() != null) {
            watchlist.setDescription(watchlistDTO.getDescription());
        }

        if (watchlistDTO.getStocks() != null) {
            List<Stock> existingStocks = watchlist.getStocks();
            List<Stock> newStocks = convertDTOsToStocks(watchlistDTO.getStocks());

            // Find stocks to add
            List<Stock> stocksToAdd = newStocks.stream()
                    .filter(stock -> existingStocks.stream().noneMatch(es -> es.getId().equals(stock.getId())))
                    .collect(Collectors.toList());

            // Find stocks to remove
            List<Stock> stocksToRemove = existingStocks.stream()
                    .filter(stock -> newStocks.stream().noneMatch(ns -> ns.getId().equals(stock.getId())))
                    .collect(Collectors.toList());

            // Find stocks to update
            for (Stock newStock : newStocks) {
                for (Stock existingStock : existingStocks) {
                    if (existingStock.getId().equals(newStock.getId())) {
                        // Update attributes of existingStock based on newStock
                        existingStock.setSymbol(newStock.getSymbol());
                        existingStock.setName(newStock.getName());
                        // ... update other attributes as needed
                    }
                }
            }

            existingStocks.addAll(stocksToAdd);
            existingStocks.removeAll(stocksToRemove);
        }

        watchlistRepository.save(watchlist);
        return convertToDTO(watchlist);
    }

    private List<Stock> convertDTOsToStocks(List<StockDTO> stockDTOs) {
        if (stockDTOs == null) {
            return Collections.emptyList();
        }

        return stockDTOs.stream()
                .map(stockDTO -> {
                    Stock stock = new Stock();
                    stock.setId(stockDTO.getId());
                    stock.setSymbol(stockDTO.getSymbol());
                    stock.setName(stockDTO.getName());
                    // Fetch key financials for the stock
                    String financialDataJson = twelveDataService.fetchCompanyFundamentals(stock.getSymbol());
                    try {
                        JSONObject financialData = new JSONObject(financialDataJson);

                        // Extracting current price
                        if (financialData.has("close")) {
                            stock.setCurrentPrice(financialData.getDouble("close"));
                        }

                        // Extracting percentage change
                        if (financialData.has("percent_change")) {
                            // Assuming you have a setPercentageChange method in the Stock entity
                            stock.setPercentageChange(financialData.getDouble("percent_change"));
                        }

                    // You can add other attributes extraction here as needed...
                    } catch (JSONException e) {
                        // Log the error or handle it as appropriate
                        System.err.println("Error parsing JSON for stock: " + stock.getSymbol());
                        e.printStackTrace();
                    }

                    return stock;
                })
                .collect(Collectors.toList());
    }




    // Other CRUD operations...
}
