package com.mybiblestudywebapp.events;

import com.mybiblestudywebapp.redis.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 4/4/20
 */
@EnableBinding(PersistenceChannel.class)
@Slf4j
@RequiredArgsConstructor
public class Handler {

    private final CacheService cacheService;

    @StreamListener("persistenceServiceTopic")
    public void subMsg(String msg) {
        log.debug("Received message: {}", msg);

        if ("INVALIDATE".equals(msg)) {
            log.debug("Invalidating cache");
            cacheService.invalidateCache();
        }
    }
}
