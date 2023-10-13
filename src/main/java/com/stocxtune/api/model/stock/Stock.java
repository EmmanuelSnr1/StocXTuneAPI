package com.stocxtune.api.model.stock;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stocks")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(nullable = false)
    private String symbol;

    @NonNull
    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2)
    private Double currentPrice;

    @Column(precision = 10, scale = 2)
    private Double previousClose;

    private Long volume;

    @Column(precision = 10, scale = 2)
    private Double priceChange;

    @Column(precision = 5, scale = 2)
    private Double percentageChange;

    @Column(precision = 10, scale = 2)
    private Double fiftyTwoWeekHigh;

    @Column(precision = 10, scale = 2)
    private Double fiftyTwoWeekLow;

    private String sector;

    @Column(precision = 5, scale = 2)
    private Double dividendYield;

    @Column(precision = 10, scale = 2)
    private Double peRatio;

    @Column(length = 500)
    private String notes;

    @Column(precision = 10, scale = 2)
    private Double targetPrice;

    @Temporal(TemporalType.DATE)
    private Date dateAdded;

    private String ownershipStatus;

    // Other attributes as needed...

    // Constructors, getters, setters, etc. are managed by Lombok annotations
}
