package com.stocxtune.api.repository;

import com.stocxtune.api.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    // Custom query to find all watchlists associated with a particular user.
    List<Watchlist> findByUserId(Long userId);

    List<Watchlist> findByUser_Email(String email);


    // Custom query to find a watchlist by its name.
    Watchlist findByName(String name);

    // Other custom query methods for Watchlist can be added here if needed.
}
