package com.stocxtune.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDTO {

    private Long id;
    private String name;
    private String description;
    private Long userId; // Assuming you just want to transfer the user's ID
    private List<TransactionDTO> transactions;
    private List<HoldingDTO> holdings;
    private String notes;

    // Other attributes as needed...
}
