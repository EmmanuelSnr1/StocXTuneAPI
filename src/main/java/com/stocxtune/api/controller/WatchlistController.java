package com.stocxtune.api.controller;

import com.stocxtune.api.dao.UserDao;
import com.stocxtune.api.dto.StockDTO;
import com.stocxtune.api.dto.WatchlistDTO;
import com.stocxtune.api.model.User;
import com.stocxtune.api.security.services.UserDetailsImpl;
import com.stocxtune.api.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    @Autowired
    private WatchlistService watchlistService;
    @Autowired
    private UserDao userDao;

    @PostMapping
    public WatchlistDTO createWatchlist(@RequestBody WatchlistDTO watchlistDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new RuntimeException("Unexpected user details type");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String email = userDetails.getEmail();

        // Fetch the user by email or ID (choose one based on your requirements)
        User user = userDao.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Set the userEmail in the WatchlistDTO
        watchlistDTO.setUser(email); // Updated this line
        return watchlistService.save(watchlistDTO);
    }



    @GetMapping("/{id}")
    public WatchlistDTO getWatchlist(@PathVariable Long id) {
        return watchlistService.findById(id);
    }

    @GetMapping
    public List<WatchlistDTO> getAllWatchlist() {
        return watchlistService.findAll();
    }
    @GetMapping("/my-watchlist")
    public List<WatchlistDTO> getAllWatchlistForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new RuntimeException("Unexpected user details type");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String email = userDetails.getEmail();
        return watchlistService.findAllByUserId(userId);
    }


    @GetMapping("/user/{userId}")
    public List<WatchlistDTO> getWatchlistByUserId(@PathVariable Long userId) {
        return watchlistService.findAllByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteWatchlist(@PathVariable Long id) {
        watchlistService.deleteById(id);
    }


    @PutMapping("/{id}")
    public WatchlistDTO updateWatchlist(@PathVariable Long id, @RequestBody WatchlistDTO watchlistDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new RuntimeException("Unexpected user details type");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String email = userDetails.getEmail();

        // Set the userEmail in the WatchlistDTO
        watchlistDTO.setUser(email); // Updated this line
        return watchlistService.update(id, watchlistDTO);
    }

    @PutMapping("/{id}/details")
    public WatchlistDTO updateWatchlistDetails(@PathVariable Long id, @RequestBody WatchlistDTO watchlistDTO) {
        return watchlistService.updateDetails(id, watchlistDTO);
    }

    @PostMapping("/{id}/stocks")
    public WatchlistDTO addStocksToWatchlist(@PathVariable Long id, @RequestBody List<StockDTO> stocks) {
        return watchlistService.addStocks(id, stocks);
    }

    @DeleteMapping("/{id}/stocks")
    public WatchlistDTO removeStocksFromWatchlist(@PathVariable Long id, @RequestBody List<Long> stockIds) {
        return watchlistService.removeStocks(id, stockIds);
    }

//    @PutMapping("/{id}/stocks")
//    public WatchlistDTO updateStockSymbols(@PathVariable Long id, @RequestBody List<StockDTO> stocks) {
//        return watchlistService.updateStocks(id, stocks);
//    }




    // Other CRUD operations...
}
