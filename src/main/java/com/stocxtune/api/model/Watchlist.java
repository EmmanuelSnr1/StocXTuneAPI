package com.stocxtune.api.model;

import com.stocxtune.api.model.stock.Stock;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "watchlists")
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    private String description;


    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "watchlist_stocks",
            joinColumns = @JoinColumn(name = "watchlist_id"),
            inverseJoinColumns = @JoinColumn(name = "stock_id"))
    private List<Stock> stocks;

    // Other attributes as needed...
    // Some ideas would be some notes on the watchlist.

}
