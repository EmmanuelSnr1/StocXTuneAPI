package com.stocxtune.api.controller;

import com.stocxtune.api.dao.UserDao;
import com.stocxtune.api.dto.HoldingDTO;
import com.stocxtune.api.dto.PortfolioDTO;
import com.stocxtune.api.dto.TransactionDTO;
import com.stocxtune.api.dto.WatchlistDTO;
import com.stocxtune.api.model.User;
import com.stocxtune.api.security.services.UserDetailsImpl;
import com.stocxtune.api.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private UserDao userDao;

    // Portfolio Endpoints:

    @PostMapping
    public ResponseEntity<PortfolioDTO> createPortfolio(@RequestBody PortfolioDTO portfolioDTO) { //Passed
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
        portfolioDTO.setUserId(userId); // Updated this line
        return ResponseEntity.ok(portfolioService.save(portfolioDTO));
    }

    @GetMapping("/my-portfolio")
    public List<PortfolioDTO> getAllPortfoliosForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new RuntimeException("Unexpected user details type");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();
        String email = userDetails.getEmail();
//        return watchlistService.findAllByUserId(userId);
        return portfolioService.findAllByUserId(userId);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDTO> getPortfolio(@PathVariable Long id) {
        return ResponseEntity.ok(portfolioService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDTO> updatePortfolio(@PathVariable Long id, @RequestBody PortfolioDTO portfolioDTO) {
        return ResponseEntity.ok(portfolioService.update(id, portfolioDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        portfolioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Holdings Endpoints:

    @PostMapping("/{id}/holdings")
    public ResponseEntity<PortfolioDTO> addHoldings(@PathVariable Long id, @RequestBody List<HoldingDTO> holdings) {
        return ResponseEntity.ok(portfolioService.addHoldings(id, holdings));
    }

    @DeleteMapping("/{portfolioId}/holdings/{holdingId}")
    public ResponseEntity<PortfolioDTO> removeHolding(@PathVariable Long portfolioId, @PathVariable Long holdingId) {
        // Assuming you'll modify the service to handle removal of a single holding by its ID
        return ResponseEntity.ok(portfolioService.removeHolding(portfolioId, holdingId));
    }

    @GetMapping("/{id}/holdings")
    public ResponseEntity<List<HoldingDTO>> getHoldingsByPortfolioId(@PathVariable Long id) {
        return ResponseEntity.ok(portfolioService.getHoldingsByPortfolioId(id));
    }

    // Transactions Endpoints:

    @PostMapping("/{id}/transactions")
    public ResponseEntity<PortfolioDTO> addTransactions(@PathVariable Long id, @RequestBody List<TransactionDTO> transactions) {
        return ResponseEntity.ok(portfolioService.addTransactions(id, transactions));
    }

    @DeleteMapping("/{portfolioId}/transactions/{transactionId}")
    public ResponseEntity<PortfolioDTO> removeTransaction(@PathVariable Long portfolioId, @PathVariable Long transactionId) {
        // Assuming you'll modify the service to handle removal of a single transaction by its ID
        return ResponseEntity.ok(portfolioService.removeTransaction(portfolioId, transactionId));
    }

    // ... [any other endpoints as needed]
}
