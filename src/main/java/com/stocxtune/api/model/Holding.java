package com.stocxtune.api.model;

import com.stocxtune.api.model.stock.Stock;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "holdings")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;

    @ManyToOne
    @JoinTable(name = "holding_stocks",
            joinColumns = @JoinColumn(name = "holding_id"),
            inverseJoinColumns = @JoinColumn(name = "stock_id"))
    private Stock stock;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    private Double quantity;

    private Double averagePrice;

    // Other attributes as needed...
}


