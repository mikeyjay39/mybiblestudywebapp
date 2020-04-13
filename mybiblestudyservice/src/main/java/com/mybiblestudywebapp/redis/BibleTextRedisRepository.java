package com.mybiblestudywebapp.redis;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/31/20
 */
public interface BibleTextRedisRepository {

    void saveVerses(String key, List<Map<String, String>> verses);
    void updateVerses(String key, List<Map<String, String>> verses);
    void deleteVerses(String key);
    List<Map<String, String>> findVerses(String key);

}
