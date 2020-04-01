package com.mybiblestudywebapp.redis;

import com.mybiblestudywebapp.persistence.PersistenceService;
import com.mybiblestudywebapp.persistence.request.GetStudyNotesForChapterRequest;
import com.mybiblestudywebapp.utils.persistence.model.Note;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 4/1/20
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CacheServiceImpl implements CacheService{

    private final PersistenceService persistenceService;

    @Override
    @Cacheable("dashboard")
    public List<Note> getStudyNotesForChapter(GetStudyNotesForChapterRequest request) {
        log.debug("{} not found in cache, calling persistence service", request);
        return persistenceService.getStudyNotesForChapter(
                request.getViewCode(),
                request.getBook(),
                request.getChapterNo()
        );
    }

    @Override
    @CacheEvict(value = "dashboard", key = "#request")
    public void clearStudyNotesForChapter(GetStudyNotesForChapterRequest request) {
    }
}
