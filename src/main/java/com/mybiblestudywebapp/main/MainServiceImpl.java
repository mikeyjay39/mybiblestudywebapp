package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.bible.GetBible;
import com.mybiblestudywebapp.bible.GetBibleService;
import com.mybiblestudywebapp.persistence.DaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private static final Logger logger = LoggerFactory.getLogger(MainServiceImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<BibleStudyResponse> getChapterAndNotes(BibleStudyRequest request) {

        String viewCode = request.getViewCode();
        String book = request.getBook();
        int chapterNo = request.getChapterNo();
        BibleStudyResponse response = new BibleStudyResponse();
        var verses = getBibleService.getVersesForChapter(book, chapterNo);
        var notes = daoService.getStudyNotesForChapter(viewCode, book, chapterNo);
        response.setBook(book);
        response.setChapter(chapterNo);

        try {
            response.setVerses(verses.get());
        } catch (Exception e) {
            String errMsg = "Could not get Bible verses \n" + e.getMessage();
            logger.error(errMsg);
            response.setErrMsg(errMsg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        try {
            response.setNotes(notes.get());
        } catch (Exception e) {
            String errMsg = "Could not get notes \n" + e.getMessage();
            logger.error(errMsg);
            response.setErrMsg(errMsg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
