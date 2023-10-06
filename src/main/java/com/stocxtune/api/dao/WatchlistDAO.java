package com.stocxtune.api.dao;

import com.stocxtune.api.dto.WatchlistDTO;

import java.util.List;

public interface WatchlistDAO {
    WatchlistDTO save(WatchlistDTO watchlistDTO);
    WatchlistDTO findById(Long id);
    List<WatchlistDTO> findAllByUserId(Long userId);
    void deleteById(Long id);
    // Other CRUD operations...
}
