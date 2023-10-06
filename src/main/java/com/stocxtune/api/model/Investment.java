package com.stocxtune.api.model;

import com.stocxtune.api.model.stock.Asset;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Asset asset;

    private Integer quantity;

    private Double netInvested;
    private Double currentValue;

    private Double netProfit;
    private Double netProfitPercentage;

    private Double averageBuyPrice;

    // Supplying just the Asset, the Quantity and the buy Price
    public Investment(Asset asset, Integer quantity, Double averageBuyPrice, User user) {

        this.user = user;
        this.asset = asset;
        this.quantity = 0;
        this.netInvested = 0.0;
        this.currentValue = 0.0;
        this.netProfit = 0.0;
        this.netProfitPercentage = 0.0;
        this.averageBuyPrice = 0.0;

        buy(quantity, averageBuyPrice);

        refresh();
    }

    // Updates asset data based on latest LTP.
    void refresh() {

        this.currentValue = this.asset.getLTP() * quantity;

        this.netProfit = netInvested - currentValue;
        this.netProfitPercentage = (netProfit / netInvested) * 100;

    }

    // Sells the asset and returns the income from the sale
    public Double sell(int quantity, Double sellingPrice) {

        this.quantity -= quantity;
        this.netInvested -= this.quantity * averageBuyPrice;

        refresh();

        return (quantity * sellingPrice);
    }

    // Selling the asset at Market price and returning the income from the sale
    public Double sell(int quantity) {
        return sell(quantity, this.asset.getLTP());
    }

    // Selling all the asset at Market Price. and returning the income from the sale
    public Double sell() {
        return sell(this.quantity, this.asset.getLTP());
    }

    // Buy a certain quantity of the asset at the specified price.
    public void buy(int quantity, Double buyPrice) {

        this.quantity += quantity;
        this.netInvested += quantity * buyPrice;
        this.averageBuyPrice = this.netInvested / this.quantity;

        refresh();

    }

    // Buy a certain quantity of the asset at the Market Price.
    public void buy(int quantity) {
        buy(quantity, this.asset.getLTP());
    }
}
