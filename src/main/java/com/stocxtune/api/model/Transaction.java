package com.stocxtune.api.model;

import com.stocxtune.api.model.stock.Stock;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import com.stocxtune.api.enums.TransactionEnums.AssetType;
import com.stocxtune.api.enums.TransactionEnums.TransactionType;



@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @Enumerated(EnumType.STRING)
    private AssetType assetType;


    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Double shares;

    private Double price;

    private Double fees;

    // Other attributes as needed...
}


