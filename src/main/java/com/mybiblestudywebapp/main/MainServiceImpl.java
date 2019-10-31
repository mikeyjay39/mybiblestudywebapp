package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.client.BibleStudyRequest;
import com.mybiblestudywebapp.client.BibleStudyResponse;
import com.mybiblestudywebapp.dashboard.notes.AddNoteResponse;
import com.mybiblestudywebapp.dashboard.notes.RankNoteRequest;
import com.mybiblestudywebapp.dashboard.notes.RankNoteResponse;
import com.mybiblestudywebapp.getbible.GetBibleService;
import com.mybiblestudywebapp.persistence.DaoService;
import com.mybiblestudywebapp.persistence.DaoServiceException;
import com.mybiblestudywebapp.persistence.model.Note;
import com.mybiblestudywebapp.persistence.model.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.mybiblestudywebapp.dashboard.users.CreateUserRequest;
import com.mybiblestudywebapp.dashboard.users.CreateUserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<Response> getChapterAndNotes(BibleStudyRequest request) {
        String viewCode = request.getViewCode();
        String book = request.getBook();
        int chapterNo = request.getChapterNo();
        BibleStudyResponse response = new BibleStudyResponse();
        var verses = getBibleService.getVersesForChapter(book, chapterNo);
        CompletableFuture<List<Note>> futureNotes;

        try {
            futureNotes = daoService.getStudyNotesForChapter(viewCode, book, chapterNo);
            response.setBook(book);
            response.setChapter(chapterNo);
            response.setVerses(verses.get());
            response.setNotes(futureNotes.get());
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            String errMsg = "Could not finish getting Bible verses and notes. Thread interrupted\n"
                    + e.getMessage();
            return interruptedExceptionHandler(e, errMsg, response);
        } catch (ExecutionException e) {
            String errMsg = "Could not get Bible verses and notes. Execution exception \n"
                    + e.getMessage();
            return executionExceptionHandler(e, errMsg, response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * {@inheritDoc}
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Response> createUserAccount(CreateUserRequest request) {
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
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            String errMsg = "Create user account " + request.getEmail() + " thread interrupted\n"
                    + e.getMessage();
            return interruptedExceptionHandler(e, errMsg, response);
        } catch (ExecutionException e) {
            String errMsg = "Create user account " + request.getEmail()
                    + " thread execution exception\n" + e.getMessage();
            return executionExceptionHandler(e, errMsg, response);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Response> addNote(Note request) {
        AddNoteResponse response = new AddNoteResponse();
        try {
            var futureResult = daoService.addNote(request);
            long result = futureResult.get();
            response.setNoteId(result);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * {@inheritDoc}
     * @param request
     * @return
     */
    @Override
    public ResponseEntity<Response> rankNote(RankNoteRequest request) {
        RankNoteResponse response = new RankNoteResponse();
        try {
            var future = daoService.rankNote(request);
            return ResponseEntity.status(HttpStatus.OK).body(future.get());
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    @Override
    public ResponseEntity<Response> login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        return ResponseEntity.ok(daoService.login(username));
    }

    /**
     * Handler for DaoService Exceptions. Sets HTTP status to 400
     * @param e
     * @param response
     * @return
     */
    private ResponseEntity<Response> daoServiceExceptionHandler(DaoServiceException e, Response response) {
        LOGGER.error(e.getMessage());
        response.getErrorResponse()
                .setTitle(e.getClass().getName())
                .setStatus(400)
                .setDetail(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Use this to handle interrupted exceptions. Sets HTTP status to 409 and sets interrupt flag for thread.
     * @param e
     * @param errMsg
     * @param response
     * @return
     */
    private ResponseEntity<Response> interruptedExceptionHandler(
            InterruptedException e, String errMsg, Response response) {

        LOGGER.error(errMsg);
        response.getErrorResponse().setTitle(e.getClass().getName())
                .setStatus(409)
                .setDetail(errMsg);
        Thread.currentThread().interrupt();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handles Execution Exception and sets HTTP status to 409.
     * @param e
     * @param errMsg
     * @param response
     * @return
     */
    private ResponseEntity<Response> executionExceptionHandler(
            ExecutionException e, String errMsg, Response response) {

        response.getErrorResponse().setTitle(e.getClass().getName())
                .setStatus(409)
                .setDetail(errMsg);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
