package com.stocxtune.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;
    private String assetType; // Using String for simplicity, but you can use the Enum directly if desired
    private String symbol;
    private Date date;
    private String transactionType; // Using String for simplicity
    private Double shares;
    private Double price;
    private Double fees;
    private String accountInfo;

    // Other attributes as needed...
}
