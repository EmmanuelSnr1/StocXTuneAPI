package com.stocxtune.api.service.impl;

import com.stocxtune.api.dto.StockDTO;
import com.stocxtune.api.repository.StockRepository;
import com.stocxtune.api.model.stock.Stock;
import com.stocxtune.api.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Override
    public StockDTO save(StockDTO stockDTO) {
        Stock stock = new Stock();
        // Convert stockDTO to stock entity and set attributes
        // ...
        stock = stockRepository.save(stock);
        return convertToDTO(stock);
    }

    @Override
    public StockDTO findById(Long id) {
        Optional<Stock> stock = stockRepository.findById(id);
        return stock.map(this::convertToDTO).orElse(null);
    }

    @Override
    public List<StockDTO> findAll() {
        return stockRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        stockRepository.deleteById(id);
    }

    private StockDTO convertToDTO(Stock stock) {
        StockDTO dto = new StockDTO();
        // Convert stock entity to stockDTO and set attributes
        // ...
        return dto;
    }

    // Other CRUD operations...
}
