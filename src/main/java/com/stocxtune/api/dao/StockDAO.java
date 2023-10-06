package com.stocxtune.api.dao;

import com.stocxtune.api.dto.StockDTO;

import java.util.List;

public interface StockDAO {
    StockDTO save(StockDTO stockDTO);
    StockDTO findById(Long id);
    List<StockDTO> findAll();
    void deleteById(Long id);
    // Other CRUD operations...
}
