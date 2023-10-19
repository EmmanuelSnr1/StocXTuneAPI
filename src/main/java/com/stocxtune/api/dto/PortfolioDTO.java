package com.stocxtune.api.dto;

import com.stocxtune.api.model.Holding;
import com.stocxtune.api.model.Transaction;
import lombok.Data;

import java.util.List;

@Data
public class PortfolioDTO {

    private Long id;
    private String name;
    private String description;
    private String user; // To associate the Portfolio with a user.
    private Long userId; // To associate the Portfolio with a user.
    private String assetType; // This will hold the string representation of the enum

    private List<TransactionDTO> transactions;
    private List<HoldingDTO> holdings;
}
