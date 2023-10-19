package com.stocxtune.api.dto;

import com.stocxtune.api.model.Portfolio;
import com.stocxtune.api.model.stock.Stock;
import lombok.Data;

import java.util.List;

@Data
public class HoldingDTO {
    private Long id;
    private Stock stock;
    private Portfolio portfolio;
    private Double quantity;
    private Double averagePrice;


}
