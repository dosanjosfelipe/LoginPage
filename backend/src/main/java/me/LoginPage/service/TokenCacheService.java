package me.LoginPage.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TokenCacheService {

    private static class CacheEntry {
        private final Long userId;
        private final LocalDateTime expiration;

        public CacheEntry(Long userId, LocalDateTime expiration) {
            this.userId = userId;
            this.expiration = expiration;
        }

        public Long getUserId() {
            return userId;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiration);
        }
    }

    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final int expirationMinutes = 10;

    public void store(String token, Long userId) {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(expirationMinutes);
        cache.put(token, new CacheEntry(userId, expiration));
    }

    public Long getUserId(String token) {
        CacheEntry entry = cache.get(token);
        if (entry == null || entry.isExpired()) {
            cache.remove(token); 
            return null;
        }
        return entry.getUserId();
    }

    public void remove(String token) {
        cache.remove(token);
    }
}
