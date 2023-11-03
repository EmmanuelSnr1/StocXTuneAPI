package com.stocxtune.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "portfolios")
@Getter
@Setter
@ToString(exclude = "transactions") // Exclude the transactions field from the generated toString method
@RequiredArgsConstructor
@NoArgsConstructor
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "portfolio_transactions",
            joinColumns = @JoinColumn(name = "portfolio_id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id"))
    private List<Transaction> transactions;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "portfolio_holdings",
            joinColumns = @JoinColumn(name = "portfolio_id"),
            inverseJoinColumns = @JoinColumn(name = "holding_id"))
    private List<Holding> holdings;

    private String notes;

    // Other attributes as needed...
}



