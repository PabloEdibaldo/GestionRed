package com.GestionRed.GestionRed.SmartOLT.config;

import com.GestionRed.GestionRed.SmartOLT.models.OnuDetailsCache;
import com.GestionRed.GestionRed.SmartOLT.services.OnuDetailsCacheService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;
@Slf4j
@Component
public class CacheScheduler {


    @Autowired
    private OnuDetailsCacheService onuDetailsCacheService;

    @PostConstruct
    public void init() {
        updateCache();
    }

    @Scheduled(fixedRate = 3 * 60 * 60 * 1000) // 3 horas
    public void updateCache() {
        log.info("Initiating cache update...");

        try {
            onuDetailsCacheService.updateCache();
            log.info("Cache update successful.");
        } catch (Exception e) {
            log.error("Error updating ONUs details cache: ");
        }
    }
}
