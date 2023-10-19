package com.stocxtune.api.service.impl;

import com.stocxtune.api.dao.UserDao;
import com.stocxtune.api.dto.HoldingDTO;
import com.stocxtune.api.dto.PortfolioDTO;
import com.stocxtune.api.dto.StockDTO;
import com.stocxtune.api.dto.TransactionDTO;
import com.stocxtune.api.model.*;
import com.stocxtune.api.model.stock.Stock;
import com.stocxtune.api.enums.TransactionEnums.AssetType;
import com.stocxtune.api.enums.TransactionEnums.TransactionType;

import com.stocxtune.api.repository.PortfolioRepository;
import com.stocxtune.api.repository.StockRepository;

import com.stocxtune.api.service.PortfolioService;
import com.stocxtune.api.service.TwelveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PortfolioRepository portfolioRepository;
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TwelveDataService twelveDataService ;


    private static final Logger logger = LoggerFactory.getLogger(PortfolioServiceImpl.class);


    @Override
    @Transactional
    public PortfolioDTO save(PortfolioDTO portfolioDTO) {
        Portfolio portfolio = new Portfolio();

        // Convert portfolioDTO to portfolio entity and set attributes
        portfolio.setName(portfolioDTO.getName());
        portfolio.setDescription(portfolioDTO.getDescription());

        // Using UserDao to fetch the User entity using the email
        User user = userDao.getUserByEmail(portfolioDTO.getUser())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + portfolioDTO.getUser()));
        portfolio.setUser(user);

//        // Convert TransactionDTOs to Transaction entities
//        if (portfolioDTO.getTransactions() != null && !portfolioDTO.getTransactions().isEmpty()) {
//            List<Transaction> transactions = portfolioDTO.getTransactions().stream()
//                    .map(transactionDTO -> {
//                        Transaction transaction = new Transaction();
////                        transaction.setStock(transactionDTO.getSymbol());
//                        transaction.setDate(transactionDTO.getDate());
//                        transaction.setShares(transactionDTO.getShares());
//                        transaction.setPrice(transactionDTO.getPrice());
//                        transaction.setFees(transactionDTO.getFees());
//                        // Set other transaction attributes from transactionDTO here...
//                        return transaction;
//                    })
//                    .collect(Collectors.toList());
//            portfolio.setTransactions(transactions);
//        }
//
//        // Similarly, you can convert other DTOs like HoldingDTO to Holding entities and set them to the portfolio
//        if (portfolioDTO.getHoldings() != null && !portfolioDTO.getHoldings().isEmpty()){
//            List<Holding> holdings = portfolioDTO.getHoldings().stream()
//                    .map(holdingDTO -> {
//                        Holding holding = new Holding();
//                        holding.setPortfolio(holdingDTO.getPortfolio());
//                        holding.setQuantity(holdingDTO.getQuantity());
//                        holding.setStock(holdingDTO.getStock());
//                        holding.setAveragePrice(holdingDTO.getAveragePrice());
//                        return holding;
//                    })
//                    .collect(Collectors.toList());
//            portfolio.setHoldings(holdings);
//        }


        portfolio = portfolioRepository.save(portfolio);
        logger.info("Saved portfolio: {}", portfolio);
        return convertToDTO(portfolio);
    }


    @Override
    public PortfolioDTO findById(Long id) {
        Optional<Portfolio> portfolio = portfolioRepository.findById(id);
        return portfolio.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<PortfolioDTO> findAll() {
        return portfolioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PortfolioDTO> findAllByUserId(Long userId) {
        List<Portfolio> portfolio = portfolioRepository.findByUserId(userId);
        return portfolio.stream()
                .map(this::convertToDTO)  // Assuming you have a method called convertToDTO
                .collect(Collectors.toList());
    }

    @Override
    public List<PortfolioDTO> getPortfolioByUserEmail(String email) {
        List<Portfolio> portfolio = portfolioRepository.findByUser_Email(email);
        // Convert the list of Watchlist entities to WatchlistDTOs and return
        return portfolio.stream()
                .map(this::convertToDTO)  // Assuming you have a method called convertToDTO
                .collect(Collectors.toList());
    }

    @Override
    public PortfolioDTO update(Long id, PortfolioDTO portfolioDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        portfolioRepository.deleteById(id);
    }

    @Override
    public PortfolioDTO updateDetails(Long id, PortfolioDTO portfolioDTO) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + id));

        if (portfolioDTO.getName() != null) {
            portfolio.setName(portfolioDTO.getName());
        }

        if (portfolioDTO.getDescription() != null) {
            portfolio.setDescription(portfolioDTO.getDescription());
        }

        portfolioRepository.save(portfolio);
        return convertToDTO(portfolio);
    }

    @Override
    public PortfolioDTO addTransactions(Long id, List<TransactionDTO> transactions) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + id));

        List<Transaction> newTransactions = convertDTOsToTransactions(transactions);
        portfolio.getTransactions().addAll(newTransactions);

        // Convert the transactions into holdings
        List<Holding> newHoldings = convertTransactionsToHoldings(transactions);

        // Add the new holdings to the portfolio
        portfolio.getHoldings().addAll(newHoldings);

        portfolioRepository.save(portfolio);
        return convertToDTO(portfolio);
    }


    @Override
    public PortfolioDTO removeTransactions(Long id, List<Long> transactionIds) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + id));

        portfolio.getTransactions().removeIf(transaction -> transactionIds.contains(transaction.getId()));

        portfolioRepository.save(portfolio);
        return convertToDTO(portfolio);
    }

    @Override
    public PortfolioDTO addHoldings(Long id, List<HoldingDTO> holdings) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + id));

        List<Holding> newHoldings = convertDTOsToHoldings(holdings);
        portfolio.getHoldings().addAll(newHoldings);

        portfolioRepository.save(portfolio);
        return convertToDTO(portfolio);
    }

    @Override
    public PortfolioDTO removeHoldings(Long id, List<Long> holdingIds) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + id));

        portfolio.getHoldings().removeIf(holding -> holdingIds.contains(holding.getId()));

        portfolioRepository.save(portfolio);
        return convertToDTO(portfolio);
    }

    @Override
    public PortfolioDTO updateHolding(Long portfolioId, Long holdingId, HoldingDTO holdingDTO) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));

        Holding holdingToUpdate = portfolio.getHoldings().stream()
                .filter(holding -> holding.getId().equals(holdingId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Holding not found with ID: " + holdingId));

        holdingToUpdate.setStock(holdingDTO.getStock());
        holdingToUpdate.setQuantity(holdingDTO.getQuantity());
        // ... update other attributes as needed

        portfolioRepository.save(portfolio);
        return convertToDTO(portfolio);
    }


    @Override
    public List<HoldingDTO> getHoldingsByPortfolioId(Long id) {
        return null;
    }

    @Override
    public PortfolioDTO removeHolding(Long portfolioId, Long holdingId) {
        return null;
    }

    @Override
    public PortfolioDTO removeTransaction(Long portfolioId, Long transactionId) {
        return null;
    }

    private PortfolioDTO convertToDTO(Portfolio portfolio) {
        PortfolioDTO portfolioDTO = new PortfolioDTO();

        // Convert portfolio entity to portfolioDTO and set attributes
        portfolioDTO.setId(portfolio.getId());
        portfolioDTO.setName(portfolio.getName());
        portfolioDTO.setDescription(portfolio.getDescription());

        // Set the userEmail from the User entity associated with the portfolio
        if (portfolio.getUser() != null) {
            portfolioDTO.setUser(portfolio.getUser().getEmail());
        }

        // Convert Transaction entities to TransactionDTOs
        if (portfolio.getTransactions() != null && !portfolio.getTransactions().isEmpty()) {
            List<TransactionDTO> transactionDTOs = portfolio.getTransactions().stream()
                    .map(transaction -> {
                        TransactionDTO transactionDTO = new TransactionDTO();
                        transactionDTO.setId(transaction.getId());
                        transactionDTO.setSymbol(transaction.getStock().getSymbol());
                        transactionDTO.setDate(transaction.getDate());
                        transactionDTO.setShares(transaction.getShares());
                        transactionDTO.setPrice(transaction.getPrice());
                        transactionDTO.setFees(transaction.getFees());
                        // Set other attributes from transaction to transactionDTO here...
                        // If you have any external data fetching similar to the stocks, you can implement it here.

                        logger.info("Converted Transaction: {}", transactionDTO);

                        return transactionDTO;
                    })
                    .collect(Collectors.toList());
            portfolioDTO.setTransactions(transactionDTOs);
        }

        // Convert Transaction entities to TransactionDTOs
        if (portfolio.getHoldings() != null && !portfolio.getHoldings().isEmpty()) {
            List<HoldingDTO> holdingDTOs = portfolio.getHoldings().stream()
                    .map(holding -> {
                        HoldingDTO holdingDTO = new HoldingDTO();
                        holdingDTO.setId(holding.getId());
                        holdingDTO.setStock(holding.getStock());
                        holdingDTO.setQuantity(holding.getQuantity());
                        holdingDTO.setAveragePrice(holding.getAveragePrice());
                        holdingDTO.setPortfolio(holding.getPortfolio());

                        logger.info("Converted Holdings: {}", holdingDTO);

                        return holdingDTO;
                    })
                    .collect(Collectors.toList());
            portfolioDTO.setHoldings(holdingDTOs);
        }

        // Similarly, you can convert other entities like Holding to DTOs and set them to the PortfolioDTO

        return portfolioDTO;
    }


    private List<Transaction> convertDTOsToTransactions(List<TransactionDTO> transactionDTOs) {
        if (transactionDTOs == null) {
            return Collections.emptyList();
        }

        return transactionDTOs.stream()
                .map(transactionDTO -> {
                    Transaction transaction = new Transaction();
                    transaction.setId(transactionDTO.getId());

                    if (transactionDTO.getAssetType() != null && AssetType.valueOf(transactionDTO.getAssetType()) == AssetType.SECURITY) {
                        // Convert StockDTO to Stock entity and always save it
                        Stock stock = convertTransactionSymbolToStock(transactionDTO.getSymbol());
                        stock = stockRepository.save(stock);  // Always save the stock
                        transaction.setStock(stock);
                        transaction.setFees(transactionDTO.getFees());
                    } else if (transactionDTO.getAssetType() != null && AssetType.valueOf(transactionDTO.getAssetType()) == AssetType.CASH) {
                        // For CASH transactions, fees are not set
                        transaction.setFees(0.0);
                    }

                    transaction.setDate(transactionDTO.getDate());
                    transaction.setShares(transactionDTO.getShares());
                    transaction.setPrice(transactionDTO.getPrice());

                    if (transactionDTO.getTransactionType() != null) {
                        transaction.setTransactionType(TransactionType.valueOf(transactionDTO.getTransactionType()));
                    }

                    // ... set other attributes as needed

                    return transaction;
                })
                .collect(Collectors.toList());
    }




    private Stock convertTransactionSymbolToStock(String symbol) {
        Stock stock = new Stock();
        stock.setSymbol(symbol);

        String financialDataJson = twelveDataService.fetchCompanyFundamentals(symbol);
        try {
            JSONObject financialData = new JSONObject(financialDataJson);

            // Extracting the Stock name
            if (financialData.has("name")) {
                stock.setName(financialData.getString("name"));
            }
            // Extracting current price
            if (financialData.has("close")) {
                stock.setCurrentPrice(financialData.getDouble("close"));
            }
            // Extracting percentage change
            if (financialData.has("percent_change")) {
                stock.setPercentageChange(financialData.getDouble("percent_change"));
            }

            // You can add other attributes extraction here as needed...

        } catch (JSONException e) {
            // Log the error or handle it as appropriate
            System.err.println("Error parsing JSON for stock: " + stock.getSymbol());
            e.printStackTrace();
        }

        return stock;
    }
    private List<Holding> convertTransactionsToHoldings(List<TransactionDTO> transactions) {
        if (transactions == null) {
            return Collections.emptyList();
        }

        // Use a map to aggregate transactions by stock symbol
        Map<String, Holding> holdingMap = new HashMap<>();

        for (TransactionDTO transactionDTO : transactions) {
            if (transactionDTO.getAssetType() != null && AssetType.valueOf(transactionDTO.getAssetType()) == AssetType.SECURITY) {
                String symbol = transactionDTO.getSymbol();
                Holding holding = holdingMap.get(symbol);

                if (holding == null) {
                    holding = new Holding();
                    holding.setAveragePrice(0.0); // Initialize averagePrice
                    holding.setQuantity(0.0);    // Initialize quantity
                }

                // Convert StockDTO to Stock entity
                Stock stock = convertTransactionSymbolToStock(symbol);
                stock = stockRepository.save(stock);  // Always save the stock
                holding.setStock(stock);

                double totalCostBefore = holding.getAveragePrice() * holding.getQuantity();
                double transactionCost = transactionDTO.getPrice() * transactionDTO.getShares();

                if (transactionDTO.getTransactionType().equals(TransactionType.BUY_LONG.name())) {
                    holding.setQuantity(holding.getQuantity() + transactionDTO.getShares());
                } else if (transactionDTO.getTransactionType().equals(TransactionType.SELL_LONG.name())) {
                    holding.setQuantity(holding.getQuantity() - transactionDTO.getShares());
                }
                // Handle other transaction types as needed...

                double totalCostAfter = totalCostBefore + transactionCost;
                if (holding.getQuantity() != 0) {
                    holding.setAveragePrice(totalCostAfter / holding.getQuantity());
                } else {
                    holding.setAveragePrice(0.0);
                }

                holdingMap.put(symbol, holding);
            }
        }

        return new ArrayList<>(holdingMap.values());
    }




    private List<Holding> convertDTOsToHoldings(List<HoldingDTO> holdingDTOs) {
        if (holdingDTOs == null) {
            return Collections.emptyList();
        }

        return holdingDTOs.stream()
                .map(holdingDTO -> {
                    Holding holding = new Holding();
                    holding.setId(holdingDTO.getId());
                    holding.setStock(holdingDTO.getStock());
                    holding.setQuantity(holdingDTO.getQuantity());
                    // ... set other attributes as needed

                    return holding;
                })
                .collect(Collectors.toList());
    }

}
