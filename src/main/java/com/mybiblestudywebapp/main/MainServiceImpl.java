package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.bible.GetBibleService;
import com.mybiblestudywebapp.persistence.DaoService;
import com.mybiblestudywebapp.persistence.model.User;
import java.util.concurrent.ExecutionException;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(MainServiceImpl.class);

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
            LOGGER.error(errMsg);
            response.setErrMsg(errMsg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        try {
            response.setNotes(notes.get());
        } catch (Exception e) {
            String errMsg = "Could not get notes \n" + e.getMessage();
            LOGGER.error(errMsg);
            response.setErrMsg(errMsg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * {@inheritDoc}
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<CreateUserAccountResponse> createUserAccount(User request) {
        CreateUserAccountResponse response = new CreateUserAccountResponse();
        User result = null;

        try {
            result = daoService.createUserAccount(request).get();
            response
                    .setUserId(result.getUserId())
                    .setEmail(result.getEmail())
                    .setFirstname(result.getFirstname())
                    .setLastname(result.getLastname());

        } catch (InterruptedException e) {
            String errMsg = "Create user account " + request.getEmail() + " thread interrupted\n"
                    + e.getMessage();
            LOGGER.error(errMsg);
            response.setTitle("Interrupted Exception")
                    .setStatus(409)
                    .setDetail(errMsg);
            ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (ExecutionException e) {
            String errMsg = "Create user account " + request.getEmail()
                    + " thread execution exception\n" + e.getMessage();
        }

        return ResponseEntity.ok(response);
    }
}
