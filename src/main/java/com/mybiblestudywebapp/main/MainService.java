package com.mybiblestudywebapp.main;

import org.springframework.http.ResponseEntity;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
public interface MainService {

    /**
     *
     * @param viewCode
     * @param book
     * @param chapter
     * @return BibleStudyResponse
     */
    ResponseEntity<BibleStudyResponse> getChapterAndNotes(String viewCode, String book, int chapter);

}
