package com.mybiblestudywebapp.redis;

import com.mybiblestudywebapp.utils.http.GetStudyNotesForChapterRequest;
import com.mybiblestudywebapp.utils.persistence.model.Note;

import java.util.List;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 4/1/20
 */
public interface CacheService {

    List<Note> getStudyNotesForChapter(GetStudyNotesForChapterRequest request);

    void clearStudyNotesForChapter(GetStudyNotesForChapterRequest request);

    void invalidateCache();


}
