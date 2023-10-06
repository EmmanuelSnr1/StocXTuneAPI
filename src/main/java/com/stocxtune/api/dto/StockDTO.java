package com.stocxtune.api.dto;

import lombok.*;

import java.util.Date;

@Data
public class StockDTO {
    private Long id;
    private String symbol;
    private String name;
    private Double currentPrice;
    private Double previousClose;
    private Long volume;
    private Double change;
    private Double percentageChange;
    private Double fiftyTwoWeekHigh;
    private Double fiftyTwoWeekLow;
    private String sector;
    private Double dividendYield;
    private Double peRatio;
    private String notes;
    private Double targetPrice;
    private String ownershipStatus;
    private Date dateAdded;
}
