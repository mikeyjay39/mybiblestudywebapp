package com.mybiblestudywebapp.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/31/20
 */
@Repository
public class BibleTextRedisRepositoryImpl implements BibleTextRedisRepository {

    private static final String HASH_NAME = "bibletext";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private HashOperations hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }


    @Override
    public void saveVerses(String key, List<Map<String, String>> verses) {
        hashOperations.put(HASH_NAME, key, verses);
    }

    @Override
    public void updateVerses(String key, List<Map<String, String>> verses) {
        hashOperations.put(HASH_NAME, key, verses);
    }

    @Override
    public void deleteVerses(String key) {
        hashOperations.delete(HASH_NAME, key);
    }

    @Override
    public List<Map<String, String>> findVerses(String key) {
        return (List<Map<String, String>>) hashOperations.get(HASH_NAME, key);
    }
}
