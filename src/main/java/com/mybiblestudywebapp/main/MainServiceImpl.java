package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.bible.GetBible;
import com.mybiblestudywebapp.bible.GetBibleService;
import com.mybiblestudywebapp.persistence.DaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
@Service
public class MainServiceImpl implements MainService {

    @Autowired
    private GetBibleService getBibleService;

    @Autowired
    private DaoService daoService;

    /**
     * {@inheritDoc}
     */
    @Override
    public BibleStudyResponse getChapterAndNotes(String viewId, String book, String chapterNo) {

        var verses = getBibleService.getVersesForChapter(book, Integer.valueOf(chapterNo));
        var notes = daoService.getStudyNotesForChapter(viewId, book, chapterNo);
        return null;
    }
}
