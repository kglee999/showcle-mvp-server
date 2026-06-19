package com.showcle.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    private final CacheManager cacheManager;

    @EventListener(ApplicationReadyEvent.class)
    public void clearCacheOnStartup() {
        cacheManager.getCacheNames()
                .forEach(name -> {
                    cacheManager.getCache(name).clear();
                    log.info("Cache Cleared: {}", name);
                });
    }
}
