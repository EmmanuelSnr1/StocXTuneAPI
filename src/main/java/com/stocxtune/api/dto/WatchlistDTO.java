package com.stocxtune.api.dto;

import lombok.*;
import java.util.List;

@Data
public class WatchlistDTO {
    private Long id;
    private String name;
    private String description;
    private String user; // To associate the watchlist with a user.
    private Long userId; // To associate the watchlist with a user.
    private List<StockDTO> stocks; // A list of stocks associated with this watchlist.
}

