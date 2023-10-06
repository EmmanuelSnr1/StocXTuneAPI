package com.stocxtune.api.controller;

import com.stocxtune.api.dto.StockDTO;
import com.stocxtune.api.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping
    public StockDTO createStock(@RequestBody StockDTO stockDTO) {
        return stockService.save(stockDTO);
    }

    @GetMapping("/{id}")
    public StockDTO getStock(@PathVariable Long id) {
        return stockService.findById(id);
    }

    @GetMapping
    public List<StockDTO> getAllStocks() {
        return stockService.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable Long id) {
        stockService.deleteById(id);
    }

    // Other CRUD operations...
}
