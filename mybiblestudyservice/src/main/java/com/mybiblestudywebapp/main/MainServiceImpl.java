package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.bible.BibleStudyRequest;
import com.mybiblestudywebapp.bible.BibleStudyResponse;
import com.mybiblestudywebapp.bible.GetChapterResponse;
import com.mybiblestudywebapp.bibletext.BibleTextService;
import com.mybiblestudywebapp.dashboard.notes.AddNoteResponse;
import com.mybiblestudywebapp.dashboard.notes.GetCommentsResponse;
import com.mybiblestudywebapp.dashboard.users.CreateUserRequest;
import com.mybiblestudywebapp.dashboard.users.GetUsersResponse;
import com.mybiblestudywebapp.dashboard.views.AddNotesToViewResponse;
import com.mybiblestudywebapp.dashboard.views.AddViewResponse;
import com.mybiblestudywebapp.dashboard.views.DeleteViewResponse;
import com.mybiblestudywebapp.dashboard.views.GetViewsResponse;
import com.mybiblestudywebapp.persistence.PersistenceService;
import com.mybiblestudywebapp.utils.http.GenericResponse;
import com.mybiblestudywebapp.utils.http.RankNoteRequest;
import com.mybiblestudywebapp.utils.http.Response;
import com.mybiblestudywebapp.utils.persistence.model.Comment;
import com.mybiblestudywebapp.utils.persistence.model.Note;
import com.mybiblestudywebapp.utils.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
@Service
public class MainServiceImpl implements MainService {

    @Autowired
    private BibleTextService bibleTextService;

    /*@Autowired
    private PasswordEncoder encoder;*/

    @Autowired
    private PersistenceService persistenceService;

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

            List<Note> notes = persistenceService.getStudyNotesForChapter(viewCode, book, chapterNo);
            response.setBook(book);
            response.setChapter(chapterNo);
            response.setVerses(verses);
            response.setNotes(notes);

            if (notes.isEmpty()) {
                var bookChapter = persistenceService.getChapter(book, chapterNo);
                response.setBookId(bookChapter.get("bookId"));
                response.setChapterId(bookChapter.get("chapterId"));
            } else {
                response.setBookId(notes.get(0).getBookId());
                response.setChapterId(notes.get(0).getChapterId());
            }

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> createUserAccount(CreateUserRequest request) {
        return null;
    }
        /*CreateUserResponse response = new CreateUserResponse();
        User requestUser = new User();
        requestUser.setEmail(request.getEmail());
        requestUser.setFirstname(request.getFirstname());
        requestUser.setLastname(request.getLastname());
        requestUser.setPassword(encoder.encode(request.getPassword()));
        User result = null;

        return ResponseEntity.ok(response);
    }*/

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> addNote(Note request) {
        AddNoteResponse response = new AddNoteResponse();
        long result = persistenceService.addNote(request);
        response.setNoteId(result);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> rankNote(RankNoteRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(persistenceService.rankNote(request));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); //get logged in username
        return ResponseEntity.ok(persistenceService.login(username));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> addView() {
        AddViewResponse response = new AddViewResponse();
        response.setViewId(persistenceService.addView());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> getChapter(String book, int chapterNo) {
        GetChapterResponse response = new GetChapterResponse();
        var futureVerses = bibleTextService.getVerses(book, chapterNo);
        response.setVerses(futureVerses);
        var chapters = persistenceService.getChapter(book, chapterNo);
        response.setBookId(chapters.get("bookId"));
        response.setChapterId(chapters.get("chapterId"));
        response.setBook(book);
        response.setChapter(chapterNo);
        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> getViews() {
        GetViewsResponse response = new GetViewsResponse();
        var views = persistenceService.getViews();
        response.setViewCodes(views);
        return ResponseEntity.ok(response);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<String> removeNoteFromView(String viewcode, long noteId) {
        String result = persistenceService.removeNoteFromView(viewcode, noteId);

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
        String result = persistenceService.deleteView(viewcode);
        response.setResult(result);
        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> getUsers() {
        GetUsersResponse response = new GetUsersResponse();
        List<User> usersList = persistenceService.getUsers();
        List<Map<String, Object>> users = new ArrayList<>();

        for (User user : usersList) {
            Map<String, Object> userRow = new HashMap<>();
            userRow.put("name", user.getFirstname() + " " + user.getLastname());
            userRow.put("userId", user.getUserId());
            users.add(userRow);
        }

        response.setUsers(users);
        return ResponseEntity.ok(response);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> addNotesToView(String viewcode, long authorId, int ranking) {
        AddNotesToViewResponse response = new AddNotesToViewResponse();
        String result = persistenceService.addNotesToView(viewcode, authorId, ranking);
        response.setResult(result);
        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> getChapterNotesForUser(String book, int chapterNo, long userId) {
        BibleStudyResponse response = new BibleStudyResponse();

        // Get Bible text
        var getBibleTextFuture = bibleTextService.getVerses(book, chapterNo);

        // Get Notes
        var getNotesFuture = persistenceService.getAllChapterNotesForUser(book, chapterNo, userId);

        // Get future values
        List<Note> notes = getNotesFuture;
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> updateNote(Note note) {
        GenericResponse response = new GenericResponse();
        response.setStatus(persistenceService.updateNote(note));
        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> deleteNote(long noteId) {
        GenericResponse response = new GenericResponse();
        String result = persistenceService.deleteNote(noteId);
        response.setStatus(result);
        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> getComments(long noteId) {
        GetCommentsResponse response = new GetCommentsResponse();
        var result = persistenceService.getComments(noteId);
        response.setComments(result);
        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> addComment(Comment comment) {
        Response response = persistenceService.addComment(comment);
        return ResponseEntity.ok(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Response> addNoteToView(String viewcode, long noteId) {
        return ResponseEntity.ok(persistenceService.addNoteToView(viewcode, noteId));
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
