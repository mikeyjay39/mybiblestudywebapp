package com.mybiblestudywebapp.persistenceservice.persistence;

import com.mybiblestudywebapp.utils.http.LoginResponse;
import com.mybiblestudywebapp.utils.http.RankNoteRequest;
import com.mybiblestudywebapp.utils.http.RankNoteResponse;
import com.mybiblestudywebapp.utils.http.Response;
import com.mybiblestudywebapp.utils.persistence.DaoServiceException;
import com.mybiblestudywebapp.utils.persistence.model.Comment;
import com.mybiblestudywebapp.utils.persistence.model.Note;
import com.mybiblestudywebapp.utils.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/22/20
 */
@RestController
@RequestMapping(path = "persistence")
public class PersistenceController {

    private final DaoService daoService;

    @Autowired
    public PersistenceController(DaoService daoService) {
        this.daoService = daoService;
    }

    @PostMapping(path = "/addUserNotesToView/{userId}/{viewId}")
    public Long addUserNotesToView(@PathVariable long userId, @PathVariable long viewId) {
        return daoService.addUserNotesToView(userId, viewId);
    }

    @GetMapping(path = "/getStudyNotesForChapter/{viewCode}/{book}/{chapterNo}")
    public List<Note> getStudyNotesForChapter(@PathVariable String viewCode,
                                              @PathVariable String book,
                                              @PathVariable long chapterNo) {

        return daoService.getStudyNotesForChapter(viewCode, book, chapterNo);
    }

    @PostMapping(path = "/addNote", consumes = "application/json", produces = "application/json")
    public long addNote(@RequestBody Note note) {
        return daoService.addNote(note);
    }

    @PostMapping(path = "/rankNote", consumes = "application/json", produces = "application/json")
    public RankNoteResponse rankNote(@RequestBody RankNoteRequest request) {
        return daoService.rankNote(request);
    }

    @PostMapping(path = "/login/{username}")
    public LoginResponse login(@PathVariable String username) {
        return daoService.login(username);
    }

    @PostMapping(path = "/addView")
    public long addView() {
        return daoService.addView();
    }

    @GetMapping(path = "/getChapter/{book}/{chapterNo}")
    public Map<String, Integer> getChapter(@PathVariable String book,
                                           @PathVariable int chapterNo) {
        return daoService.getChapter(book, chapterNo);
    }

    @GetMapping(path = "/getViews")
    public List<String> getViews() {
        return daoService.getViews();
    }

    @DeleteMapping(path = "/removeNoteFromView/{viewcode}/{noteId}")
    public String removeNoteFromView(@PathVariable String viewcode,
                                     @PathVariable long noteId) {
        return daoService.removeNoteFromView(viewcode, noteId);
    }

    @DeleteMapping(path = "/deleteView/{viewcode}")
    public String deleteView(@PathVariable String viewcode) {
        return daoService.deleteView(viewcode);
    }

    @GetMapping(path = "/getUsers")
    public List<User> getUsers() {
        return daoService.getUsers();
    }

    @PostMapping(path = "/addNotesToView/{viewcode}/{authorId}/{ranking}")
    public String addNotesToView(@PathVariable String viewcode,
                                 @PathVariable long authorId,
                                 @PathVariable int ranking) {
        return daoService.addNotesToView(viewcode, authorId, ranking);
    }

    @GetMapping(path = "/getAllChapterNotesForUser/{book}/{chapterNo}/{userId}")
    public List<Note> getAllChapterNotesForUser(@PathVariable String book,
                                                @PathVariable long chapterNo,
                                                @PathVariable long userId) {
        return daoService.getAllChapterNotesForUser(book, chapterNo, userId);
    }

    @PutMapping(path = "/updateNote", consumes = "application/json", produces = "application/json")
    public String updateNote(@RequestBody Note note) {
        return daoService.updateNote(note);
    }

    @DeleteMapping(path = "/deleteNote/{noteId}")
    public String deleteNote(@PathVariable long noteId) {
        return daoService.deleteNote(noteId);
    }

    @GetMapping(path = "/getComments/{noteId}")
    public List<Comment> getComments(@PathVariable long noteId) {
        return daoService.getComments(noteId);
    }

    @PostMapping(path = "/addComment", consumes = "application/json", produces = "application/json")
    public Response addComment(@RequestBody Comment comment) {
        return daoService.addComent(comment);
    }

    @PostMapping(path = "/addNoteToView/{viewcode}/{noteId}")
    public Response addNoteToView(@PathVariable String viewcode,
                                  @PathVariable long noteId) {
        return daoService.addNoteToView(viewcode, noteId);
    }
}
