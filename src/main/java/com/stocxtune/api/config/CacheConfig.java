package com.stocxtune.api.config;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
                "symbols",
                "companyProfiles",
                "institutionOwnerships",
                "secFilings",
                "insiderHoldings",
                "keyFinancials",
                "keyStats",
                "stockNews",
                "marketNews",
                "marketInsiderTrades",
                "timeSeriesData",
                "marketActives",
                "marketLosers",
                "marketGainers",
                "companyFundamentals",
                "companyLogos",
                "findWatchlistByID",
                "findAllWatchlist",
//                "findWatchlistByUserID",
                "updateWatchlist",
                "deleteWatchlist",
                "saveWatchlist",
                "getWatchlistByUserEmail",
                "findPortfolioByID",
                "savePortfolio"
        );
    }

    @Scheduled(fixedRate = 86400000)  // 24 hours in milliseconds
    public void clearAllCaches() {
        CacheManager manager = cacheManager();
        if (manager != null) {
            manager.getCacheNames().forEach(name -> {
                Cache cache = manager.getCache(name);
                if (cache != null) {
                    cache.clear();
                } else {
                    System.err.println("Cache with name " + name + " not found.");
                }
            });
        } else {
            System.err.println("CacheManager is null.");
        }
    }

}
