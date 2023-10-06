package com.stocxtune.api.controller;

import com.stocxtune.api.dao.UserDao;
import com.stocxtune.api.dto.WatchlistDTO;
import com.stocxtune.api.model.User;
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
        String userEmail = authentication.getName();

        User user = userDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        // Set the userEmail in the WatchlistDTO
        watchlistDTO.setUserEmail(userEmail); // Updated this line

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
        String userEmail = authentication.getName();

        User user = userDao.getUserByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        // Set the userEmail in the WatchlistDTO
        watchlistDTO.setUserEmail(userEmail); // Updated this line
        return watchlistService.update(id, watchlistDTO);
    }




    // Other CRUD operations...
}
