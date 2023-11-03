package com.stocxtune.api.model;

import com.stocxtune.api.model.stock.Stock;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import com.stocxtune.api.enums.TransactionEnums.AssetType;
import com.stocxtune.api.enums.TransactionEnums.TransactionType;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@ToString(exclude = "portfolios") // Exclude the transactions field from the generated toString method
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol; // Added symbol field

    @ManyToOne
    @JoinTable(name = "transaction_stocks",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "stock_id"))
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Enumerated(EnumType.STRING)
    private AssetType assetType;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Double shares;

    private Double price;

    private Double fees;

    private String accountInfo;

    // Other attributes as needed...
}



