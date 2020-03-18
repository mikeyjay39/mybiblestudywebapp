package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.bible.BibleStudyRequest;
import com.mybiblestudywebapp.bible.BibleStudyResponse;
import com.mybiblestudywebapp.bible.GetChapterResponse;
import com.mybiblestudywebapp.bibletext.BibleTextService;
import com.mybiblestudywebapp.dashboard.notes.AddNoteResponse;
import com.mybiblestudywebapp.dashboard.notes.GetCommentsResponse;
import com.mybiblestudywebapp.dashboard.notes.RankNoteRequest;
import com.mybiblestudywebapp.dashboard.notes.RankNoteResponse;
import com.mybiblestudywebapp.dashboard.users.GetUsersResponse;
import com.mybiblestudywebapp.dashboard.views.AddNotesToViewResponse;
import com.mybiblestudywebapp.dashboard.views.AddViewResponse;
import com.mybiblestudywebapp.dashboard.views.DeleteViewResponse;
import com.mybiblestudywebapp.dashboard.views.GetViewsResponse;
import com.mybiblestudywebapp.persistence.Dao;
import com.mybiblestudywebapp.persistence.DaoService;
import com.mybiblestudywebapp.persistence.DaoServiceException;
import com.mybiblestudywebapp.persistence.model.Comment;
import com.mybiblestudywebapp.persistence.model.Note;
import com.mybiblestudywebapp.persistence.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private BibleTextService bibleTextService;

    @Autowired
    private DaoService daoService;

    @Autowired
    private Dao bookDao;

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
        var verses = bibleTextService.getVerses(book, chapterNo);
        CompletableFuture<List<Note>> futureNotes;

        try {
            futureNotes = daoService.getStudyNotesForChapter(viewCode, book, chapterNo);
            List<Note> notes = futureNotes.get();
            response.setBook(book);
            response.setChapter(chapterNo);
            response.setVerses(verses);
            response.setNotes(notes);

            if (notes.isEmpty()) {
                var bookChapter = daoService.getChapter(book, chapterNo).get();
                response.setBookId(bookChapter.get("bookId"));
                response.setChapterId(bookChapter.get("chapterId"));
            } else {
                response.setBookId(notes.get(0).getBookId());
                response.setChapterId(notes.get(0).getChapterId());
            }

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

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        return ResponseEntity.ok(daoService.login(username));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> addView() {
        AddViewResponse response = new AddViewResponse();

        try {
            var future = daoService.addView();
            response.setViewId(future.get());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> getChapter(String book, int chapterNo) {
        GetChapterResponse response = new GetChapterResponse();
        var futureVerses = bibleTextService.getVerses(book, chapterNo);

        try {
            var futureChapters = daoService.getChapter(book, chapterNo);
            response.setVerses(futureVerses);
            var chapters = futureChapters.get();
            response.setBookId(chapters.get("bookId"));
            response.setChapterId(chapters.get("chapterId"));
            response.setBook(book);
            response.setChapter(chapterNo);
            return ResponseEntity.ok(response);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> getViews() {
        GetViewsResponse response = new GetViewsResponse();

        try {
            var views = daoService.getViews();
            response.setViewCodes(views);
            return ResponseEntity.ok(response);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> removeNoteFromView(String viewcode, long noteId) {
        String result = daoService.removeNoteFromView(viewcode, noteId);

        if ("success".equals(result)) {
            return ResponseEntity.ok(result);
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> deleteView(String viewcode) {

        DeleteViewResponse response = new DeleteViewResponse();

        try {
            String result = daoService.deleteView(viewcode).get();
            response.setResult(result);
            return ResponseEntity.ok(response);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> getUsers() {
        GetUsersResponse response = new GetUsersResponse();

        try {
            List<User> usersList = daoService.getUsers().get();
            List<Map<String, Object>> users = new ArrayList<>();

            for (User user : usersList) {
                Map<String, Object> userRow = new HashMap<>();
                userRow.put("name", user.getFirstname() + " " + user.getLastname());
                userRow.put("userId", user.getUserId());
                users.add(userRow);
            }

            response.setUsers(users);
            return ResponseEntity.ok(response);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> addNotesToView(String viewcode, long authorId, int ranking) {
        AddNotesToViewResponse response = new AddNotesToViewResponse();

        try {
            String result = daoService.addNotesToView(viewcode, authorId, ranking).get();
            response.setResult(result);
            return ResponseEntity.ok(response);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> getChapterNotesForUser(String book, int chapterNo, long userId) {
        BibleStudyResponse response = new BibleStudyResponse();

        try {
            // Get Bible text
            var getBibleTextFuture = bibleTextService.getVerses(book, chapterNo);;

            // Get Notes
            var getNotesFuture = daoService.getAllChapterNotesForUser(book, chapterNo, userId);

            // Get future values
            List<Note> notes = getNotesFuture.get();
            var bibleText = getBibleTextFuture;

            // Set response
            response.setBook(book);
            response.setChapter(chapterNo);

            if (!notes.isEmpty()) {
                response.setBookId(notes.get(0).getBookId());
                response.setChapterId(notes.get(0).getChapterId());
            }

            response.setVerses(bibleText);
            response.setNotes(notes);
            return ResponseEntity.ok(response);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> updateNote(Note note) {
        GenericResponse response = new GenericResponse();

        try {
            response.setStatus(daoService.updateNote(note).get());
            return ResponseEntity.ok(response);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> deleteNote(long noteId) {
        GenericResponse response = new GenericResponse();

        try {
            String result = daoService.deleteNote(noteId).get();
            response.setStatus(result);
            return ResponseEntity.ok(response);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> getComments(long noteId) {
        GetCommentsResponse response = new GetCommentsResponse();

        try {
            var result = daoService.getComments(noteId);
            response.setComments(result.get());
            return ResponseEntity.ok(response);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> addComment(Comment comment) {
        Response response = new GenericResponse();

        try {
            Response daoResponse = daoService.addComent(comment).get();
            return ResponseEntity.ok(daoResponse);
        } catch (DaoServiceException e) {
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> addNoteToView(String viewcode, long noteId) {
        GenericResponse response = new GenericResponse();

        try {
            return ResponseEntity.ok(daoService.addNoteToView(viewcode, noteId).get());
        } catch (DaoServiceException e) {
            response.setStatus(e.getMessage());
            return daoServiceExceptionHandler(e, response);
        } catch (InterruptedException e) {
            return interruptedExceptionHandler(e, e.getMessage(), response);
        } catch (ExecutionException e) {
            return executionExceptionHandler(e, e.getMessage(), response);
        }
    }

    /**
     * Handler for DaoService Exceptions. Sets HTTP status to 400
     *
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
     *
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
     *
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
