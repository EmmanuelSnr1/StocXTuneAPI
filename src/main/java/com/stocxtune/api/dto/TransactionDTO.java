package com.stocxtune.api.dto;

import com.stocxtune.api.model.Portfolio;
import com.stocxtune.api.model.stock.Stock;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TransactionDTO {
    private Long id;
    private Portfolio portfolio;
    private String symbol;
    private StockDTO stock;
    private Date date;
    private Double shares;
    private Double price;
    private Double fees;
    private String transactionType; // This will hold the string representation of the enum
    private String assetType; // This will hold the string representation of the enum


}
