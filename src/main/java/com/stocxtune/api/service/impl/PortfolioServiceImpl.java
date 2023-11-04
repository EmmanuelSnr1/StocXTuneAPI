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

import com.stocxtune.api.repository.HoldingRepository;
import com.stocxtune.api.repository.PortfolioRepository;
import com.stocxtune.api.repository.StockRepository;

import com.stocxtune.api.repository.TransactionRepository;
import com.stocxtune.api.service.PortfolioService;
import com.stocxtune.api.service.TwelveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;
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
    private TransactionRepository transactionRepository;
    @Autowired
    private HoldingRepository holdingRepository;



    @Autowired
    private TwelveDataService twelveDataService ;


    private static final Logger logger = LoggerFactory.getLogger(PortfolioServiceImpl.class);



    // Portfolio Related methods
    @Override
    @Transactional
    public PortfolioDTO save(PortfolioDTO portfolioDTO) {
        // Create a new Portfolio entity
        Portfolio portfolio = new Portfolio();

        // Set the name and description from the DTO
        portfolio.setName(portfolioDTO.getName());
        portfolio.setDescription(portfolioDTO.getDescription());

        // Fetch the user by email and set it to the portfolio
        User user = userDao.getUserById(portfolioDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + portfolioDTO.getUserId()));
        portfolio.setUser(user);

        // Save the portfolio entity
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        // Convert the saved portfolio entity to DTO and return
        PortfolioDTO savedPortfolioDTO = new PortfolioDTO();
        savedPortfolioDTO.setId(savedPortfolio.getId());
        savedPortfolioDTO.setName(savedPortfolio.getName());
        savedPortfolioDTO.setDescription(savedPortfolio.getDescription());
        savedPortfolioDTO.setUserId(savedPortfolio.getUser().getId());

        return savedPortfolioDTO;
    }

    @Override
    public PortfolioDTO findById(Long id) {
        return null;
    }

    @Override
    public List<PortfolioDTO> findAll() {
        return null;
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
        return null;
    }

    @Override
    public PortfolioDTO update(Long id, PortfolioDTO portfolioDTO) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public PortfolioDTO updateDetails(Long id, PortfolioDTO portfolioDTO) {
        return null;
    }

    @Override
    @Transactional
    public PortfolioDTO addTransactions(Long portfolioId, List<TransactionDTO> transactionDTOs) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));

        List<Transaction> newTransactions = convertDTOsToTransactions(portfolioId, transactionDTOs);
        portfolio.getTransactions().addAll(newTransactions);


    //We're computing Holdings on the fly now. so we might have to hold off this for a while.
//        for (TransactionDTO transactionDTO : transactionDTOs) {
//            try {
//                AssetType assetType = AssetType.valueOf(transactionDTO.getAssetType());
//                switch (assetType) {
//                    case SECURITY:
//                        logger.info("Yes the asset type is a Security");
//                        List<Holding> newHoldings = convertTransactionDTOsToHoldings(transactionDTOs);
//                        portfolio.getHoldings().addAll(newHoldings);
//                        break;
//                    case CASH:
//                        logger.info("Yes the asset type is a Cash");
//                        break;
//                    default:
//                        // If the asset type is known but not handled, you can add additional cases.
//                        logger.info("Asset type is known but not handled in this switch case");
//                        break;
//                }
//            } catch (IllegalArgumentException e) {
//                // This block will catch the exception thrown by valueOf if the asset type is not valid.
//                throw new IllegalArgumentException("Unknown asset type: " + transactionDTO.getAssetType());
//            }
//        }



        // Convert the updated portfolio to DTO and return
        portfolioRepository.save(portfolio);
        return convertToDTO(portfolio);
    }






    @Override
    public PortfolioDTO removeTransaction(Long portfolioId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + transactionId));

        // Update the corresponding holding
        Holding holding = holdingRepository.findByPortfolioAndStock(transaction.getPortfolio(), transaction.getStock())
                .orElseThrow(() -> new RuntimeException("Holding not found for the given transaction"));

        if (transaction.getTransactionType().equals(TransactionType.BUY)) {
            holding.setQuantity(holding.getQuantity() - transaction.getShares());
            // Recalculate averagePrice, if needed
            // ...
        } else if (transaction.getTransactionType().equals(TransactionType.SELL)) {
            holding.setQuantity(holding.getQuantity() + transaction.getShares());
            // Recalculate averagePrice, if needed
            // ...
        }

        // Save the updated holding
        holdingRepository.save(holding);

        // Remove the transaction
        transactionRepository.delete(transaction);

        // Convert the updated portfolio to DTO and return
        PortfolioDTO updatedPortfolioDTO = convertToDTO(transaction.getPortfolio());
        return updatedPortfolioDTO;
    }








//    @Override
//    public PortfolioDTO addTransactions(Long id, List<TransactionDTO> transactions) {
//        return null;
//    }

    @Override
    public PortfolioDTO removeTransactions(Long id, List<Long> transactionIds) {
        return null;
    }

    @Override
    public PortfolioDTO addHoldings(Long id, List<HoldingDTO> holdings) {
        return null;
    }

    @Override
    public PortfolioDTO removeHoldings(Long id, List<Long> holdingIds) {
        return null;
    }

    @Override
    public PortfolioDTO updateHolding(Long portfolioId, Long holdingId, HoldingDTO holdingDTO) {
        return null;
    }

    @Override
    public List<HoldingDTO> getHoldingsByPortfolioId(Long portfolioId) {
        // Retrieve transactions related to the portfolio
        List<Transaction> transactions = transactionRepository.findByPortfolioId(portfolioId);
        logger.info("The transactions for the portfolio " + portfolioId + " Are " +  transactions.size());

        // Calculate holdings based on transactions
        List<HoldingDTO> holdings = calculateHoldings(transactions);

        // Return the list of HoldingDTOs
        return holdings;
    }

    @Override
    public PortfolioDTO removeHolding(Long portfolioId, Long holdingId) {
        return null;
    }

//    @Override
//    public PortfolioDTO removeTransaction(Long portfolioId, Long transactionId) {
//        return null;
//    }


    // Helper Methods.
    private List<Transaction> convertDTOsToTransactions(Long portfolioId, List<TransactionDTO> transactionDTOs) {
        if (transactionDTOs == null) {
            return Collections.emptyList();
        }

        // Fetch the Portfolio entity once before the loop
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio not found with id: " + portfolioId));


        return transactionDTOs.stream()
                .map(transactionDTO -> {
                    Transaction transaction = new Transaction();
                    transaction.setId(transactionDTO.getId());
                    transaction.setDate(transactionDTO.getDate());
                    transaction.setSymbol(transactionDTO.getSymbol());
                    transaction.setShares(transactionDTO.getShares());
                    transaction.setPrice(transactionDTO.getPrice());
                    transaction.setFees(transactionDTO.getFees());

                    transaction.setPortfolio(portfolio);

                    // Convert string representation of enums to actual enum values
                    if (transactionDTO.getAssetType() != null) {
                        transaction.setAssetType(AssetType.valueOf(transactionDTO.getAssetType()));
                    }
                    if (transactionDTO.getTransactionType() != null) {
                        transaction.setTransactionType(TransactionType.valueOf(transactionDTO.getTransactionType()));
                    }

                    return transaction;
                })
                .collect(Collectors.toList());
    }

    private TransactionDTO convertTransactionToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAssetType(transaction.getAssetType().name());
        dto.setDate(transaction.getDate());
        dto.setTransactionType(transaction.getTransactionType().name());
        dto.setShares(transaction.getShares());
        dto.setPrice(transaction.getPrice());
        dto.setFees(transaction.getFees());

        return dto;
    }

    //sets values for holdings to be returned to user
    private HoldingDTO convertHoldingToDTO(Holding holding) {
        HoldingDTO dto = new HoldingDTO();

        dto.setSymbol(holding.getSymbol());
        dto.setQuantity(holding.getQuantity());
        dto.setAveragePrice(holding.getAveragePrice());

        return dto;
    }

    private PortfolioDTO convertToDTO(Portfolio portfolio) {
        PortfolioDTO dto = new PortfolioDTO();

        dto.setId(portfolio.getId());
        dto.setName(portfolio.getName());
        dto.setDescription(portfolio.getDescription());
        dto.setUserId(portfolio.getUser().getId());
        dto.setNotes(portfolio.getNotes());

        // Convert transactions list
        List<TransactionDTO> transactionDTOs = portfolio.getTransactions().stream()
                .map(this::convertTransactionToDTO)
                .collect(Collectors.toList());
        dto.setTransactions(transactionDTOs);

        // Convert holdings list
        List<HoldingDTO> holdingDTOs = portfolio.getHoldings().stream()
                .map(this::convertHoldingToDTO)
                .collect(Collectors.toList());
        dto.setHoldings(holdingDTOs);

        return dto;
    }
    private List<Holding> convertTransactionDTOsToHoldings(List<TransactionDTO> transactionDTOs) {
        if (transactionDTOs == null) {
            return Collections.emptyList();
        }

        // Use a map to group transactions by symbol
        Map<String, List<TransactionDTO>> groupedTransactions = transactionDTOs.stream()
                .collect(Collectors.groupingBy(TransactionDTO::getSymbol));

        // Process each group of transactions and create/update the corresponding Holding
        List<Holding> holdings = new ArrayList<>();
        for (Map.Entry<String, List<TransactionDTO>> entry : groupedTransactions.entrySet()) {
            String symbol = entry.getKey();
            List<TransactionDTO> transactionsForSymbol = entry.getValue();

            Holding holding = new Holding();
            holding.setSymbol(symbol);

            double totalShares = 0;
            double totalCost = 0;

            for (TransactionDTO transactionDTO : transactionsForSymbol) {
                double shares = (transactionDTO.getShares() != null) ? transactionDTO.getShares() : 0.0;
                double price = (transactionDTO.getPrice() != null) ? transactionDTO.getPrice() : 0.0;
                double fees = (transactionDTO.getFees() != null) ? transactionDTO.getFees() : 0.0;

                if (TransactionType.BUY.name().equalsIgnoreCase(transactionDTO.getTransactionType())) {
                    totalShares += shares;
                    totalCost += (shares * price) + fees;
                } else if (TransactionType.SELL.name().equalsIgnoreCase(transactionDTO.getTransactionType())) {
                    totalShares -= shares;
                    totalCost -= (shares * price) - fees;  // Assuming you get money back after selling
                }
            }

            holding.setQuantity(totalShares);
            if (totalShares != 0) {
                holding.setAveragePrice(totalCost / totalShares);
            } else {
                holding.setAveragePrice(0.0);
            }

            holdings.add(holding);
        }

        return holdings;
    }


    private List<HoldingDTO> calculateHoldings(List<Transaction> transactions) {
        // Filter transactions by asset type 'SECURITY'
        List<Transaction> securityTransactions = transactions.stream()
                .filter(t -> AssetType.SECURITY.equals(t.getAssetType()))
                .collect(Collectors.toList());

        // Group transactions by symbol
        Map<String, List<Transaction>> transactionsBySymbol = securityTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::getSymbol));

        // Calculate holdings for each symbol
        List<HoldingDTO> holdings = new ArrayList<>();
        transactionsBySymbol.forEach((symbol, transactionList) -> {
            double totalShares = 0;
            double totalCost = 0;

            for (Transaction transaction : transactionList) {
                double transactionAmount = transaction.getShares() * transaction.getPrice();
                // If it's a buy transaction, add the shares and cost, if it's a sell, subtract them
                if (TransactionType.BUY.equals(transaction.getTransactionType())) {
                    totalShares += transaction.getShares();
                    totalCost += transactionAmount;
                } else if (TransactionType.SELL.equals(transaction.getTransactionType())) {
                    totalShares -= transaction.getShares();
                    totalCost -= transactionAmount;
                }
            }

            // Only add to holdings if the total shares are greater than 0
            if (totalShares > 0) {
                HoldingDTO holding = new HoldingDTO();
                holding.setSymbol(symbol);
                holding.setQuantity(totalShares);
                // Calculate the average price based on the total cost and total shares
                double averagePrice = totalCost / totalShares;
                holding.setAveragePrice(averagePrice);

                holdings.add(holding);
            }
        });

        return holdings;
    }



}
