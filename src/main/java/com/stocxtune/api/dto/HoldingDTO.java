package com.stocxtune.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldingDTO {

    private Long id;
    private StockDTO stock;
    private String symbol;
    private Double quantity;
    private Double averagePrice;

    // Other attributes as needed...
}
