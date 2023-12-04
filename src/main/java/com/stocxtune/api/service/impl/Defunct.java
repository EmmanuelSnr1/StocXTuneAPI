package com.stocxtune.api.service.impl;

public class Defunct {

    //    @Override
//    @Transactional
//    public PortfolioDTO addTransactions(Long portfolioId, List<TransactionDTO> transactionDTOs) {
//        Portfolio portfolio = portfolioRepository.findById(portfolioId)
//                .orElseThrow(() -> new RuntimeException("Portfolio not found with ID: " + portfolioId));
//
//        List<Transaction> newTransactions = convertDTOsToTransactions(portfolioId, transactionDTOs);
//        portfolio.getTransactions().addAll(newTransactions);
//
//
//    //We're computing Holdings on the fly now. so we might have to hold off this for a while.
////        for (TransactionDTO transactionDTO : transactionDTOs) {
////            try {
////                AssetType assetType = AssetType.valueOf(transactionDTO.getAssetType());
////                switch (assetType) {
////                    case SECURITY:
////                        logger.info("Yes the asset type is a Security");
////                        List<Holding> newHoldings = convertTransactionDTOsToHoldings(transactionDTOs);
////                        portfolio.getHoldings().addAll(newHoldings);
////                        break;
////                    case CASH:
////                        logger.info("Yes the asset type is a Cash");
////                        break;
////                    default:
////                        // If the asset type is known but not handled, you can add additional cases.
////                        logger.info("Asset type is known but not handled in this switch case");
////                        break;
////                }
////            } catch (IllegalArgumentException e) {
////                // This block will catch the exception thrown by valueOf if the asset type is not valid.
////                throw new IllegalArgumentException("Unknown asset type: " + transactionDTO.getAssetType());
////            }
////        }
//
//
//
//        // Convert the updated portfolio to DTO and return
//        portfolioRepository.save(portfolio);
//        return convertToDTO(portfolio);
//    }
}
