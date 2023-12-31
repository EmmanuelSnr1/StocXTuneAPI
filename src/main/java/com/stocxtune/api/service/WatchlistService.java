package com.stocxtune.api.service;

import com.stocxtune.api.dto.StockDTO;
import com.stocxtune.api.dto.WatchlistDTO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface WatchlistService {
    @Cacheable(value = "saveWatchlist")
    WatchlistDTO save(WatchlistDTO watchlistDTO);
    @Cacheable(value = "findWatchlistByID")
    WatchlistDTO findById(Long id);
    @Cacheable(value = "findAllWatchlist")
    List<WatchlistDTO> findAll();
//    @Cacheable(value = "findWatchlistByUserID")
    List<WatchlistDTO> findAllByUserId(Long userId);

    @Cacheable(value = "getWatchlistByUserEmail")
    List<WatchlistDTO> getWatchlistByUserEmail(String email);

    @Cacheable(value = "updateWatchlist")
    WatchlistDTO update(Long id, WatchlistDTO watchlistDTO );
    @Cacheable(value = "deleteWatchlist")
    void deleteById(Long id);

    WatchlistDTO updateDetails(Long id, WatchlistDTO watchlistDTO);

    WatchlistDTO addStocks(Long id, List<StockDTO> stocks);

    WatchlistDTO removeStocks(Long id, List<Long> stockIds);

//    WatchlistDTO updateStocks(Long id, List<StockDTO> stocks);


    // Other CRUD operations...
}
