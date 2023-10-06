package com.stocxtune.api.service;

import com.stocxtune.api.dto.WatchlistDTO;

import java.util.List;

public interface WatchlistService {
    WatchlistDTO save(WatchlistDTO watchlistDTO);
    WatchlistDTO findById(Long id);
    List<WatchlistDTO> findAll();
    List<WatchlistDTO> findAllByUserId(Long userId);

    WatchlistDTO update(Long id, WatchlistDTO watchlistDTO );
    void deleteById(Long id);
    // Other CRUD operations...
}
