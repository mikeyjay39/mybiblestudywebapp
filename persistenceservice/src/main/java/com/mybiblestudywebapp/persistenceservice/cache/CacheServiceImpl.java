package com.mybiblestudywebapp.persistenceservice.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 4/1/20
 */
@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    @CacheEvict(value = "dashboard", allEntries = true)
    public void clearDashboardCache() {
        log.debug("Evicting dashboard cache");
    }
}
