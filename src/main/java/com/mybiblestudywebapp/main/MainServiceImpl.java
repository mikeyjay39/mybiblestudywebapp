package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.getbible.GetBibleService;
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
    public ResponseEntity<? extends Response> getChapterAndNotes(BibleStudyRequest request) {

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

        } catch (InterruptedException e) {
            String errMsg = "Could not get Bible verses. Thread interrupted\n"
                    + e.getMessage();
            LOGGER.error(errMsg);
            response.getErrorResponse().setTitle("Interrupted Exception")
                    .setStatus(409)
                    .setDetail(errMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (ExecutionException e) {
            String errMsg = "Could not get Bible verses. \n"
                    + e.getMessage();
            response.getErrorResponse().setTitle("Execution Exception")
                    .setStatus(409)
                    .setDetail(errMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        try {
            response.setNotes(notes.get());

        } catch (InterruptedException e) {
            String errMsg = "Could not get Notes. Thread interrupted\n"
                    + e.getMessage();
            LOGGER.error(errMsg);
            response.getErrorResponse().setTitle("Interrupted Exception")
                    .setStatus(409)
                    .setDetail(errMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (ExecutionException e) {
            String errMsg = "Could not get Notes. \n"
                    + e.getMessage();
            response.getErrorResponse().setTitle("Execution Exception")
                    .setStatus(409)
                    .setDetail(errMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * {@inheritDoc}
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<? extends Response> createUserAccount(CreateUserRequest request) {
        CreateUserResponse response = new CreateUserResponse();
        User requestUser = new User();
        requestUser.setEmail(request.getEmail());
        requestUser.setFirstname(request.getFirstname());
        requestUser.setLastname(request.getLastname());
        requestUser.setPassword(request.getPassword());
        User result = null;

        try {
            result = daoService.createUserAccount(requestUser).get();
            response
                    .setUserId(result.getUserId())
                    .setEmail(result.getEmail())
                    .setFirstname(result.getFirstname())
                    .setLastname(result.getLastname());

        } catch (InterruptedException e) {
            String errMsg = "Create user account " + request.getEmail() + " thread interrupted\n"
                    + e.getMessage();
            LOGGER.error(errMsg);
            response.getErrorResponse().setTitle("Interrupted Exception")
                    .setStatus(409)
                    .setDetail(errMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (ExecutionException e) {
            String errMsg = "Create user account " + request.getEmail()
                    + " thread execution exception\n" + e.getMessage();
            response.getErrorResponse().setTitle("Execution Exception")
                    .setStatus(409)
                    .setDetail(errMsg);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        return ResponseEntity.ok(response);
    }
}
