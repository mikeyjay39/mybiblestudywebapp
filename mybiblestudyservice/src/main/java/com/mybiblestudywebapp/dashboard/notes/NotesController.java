package com.mybiblestudywebapp.dashboard.notes;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.utils.http.RankNoteRequest;
import com.mybiblestudywebapp.utils.http.Response;
import com.mybiblestudywebapp.utils.persistence.model.Comment;
import com.mybiblestudywebapp.utils.persistence.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/29/19
 */
@RestController
@RequestMapping("notes")
public class NotesController {

    @Autowired
    private MainService mainService;

    /**
     * Get all the chapter notes for the logged in user along with the Bible text
     *
     * @param book
     * @param chapterNo
     * @return
     */
    @GetMapping(path = "/mynotes/{book}/{chapterNo}/{userId}")
    public ResponseEntity<Response> getUserNotes(@PathVariable String book,
                                                 @PathVariable int chapterNo,
                                                 @PathVariable long userId) {
        return mainService.getChapterNotesForUser(book, chapterNo, userId);
    }

    /**
     * Get comments for a note
     *
     * @param noteId
     * @return
     */
    @GetMapping(path = "/comments/{noteId}")
    public ResponseEntity<Response> getComments(@PathVariable long noteId) {
        return mainService.getComments(noteId);
    }

    @PostMapping(path = "/comments", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> addComment(@RequestBody Comment comment) {
        return mainService.addComment(comment);
    }

    /**
     * Endpoint for adding new notes
     *
     * @param request
     * @return
     */
    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> addNote(@RequestBody Note request) {
        return mainService.addNote(request);
    }

    /**
     * Endpoint for ranking notes
     *
     * @param request
     * @return
     */
    @PostMapping(path = "/rank", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> rankNote(@RequestBody RankNoteRequest request) {
        return mainService.rankNote(request);
    }

    /**
     * Endpoint for updating notes
     *
     * @param note
     * @return
     */
    @PutMapping(path = "/update", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> updateNote(@RequestBody Note note) {
        return mainService.updateNote(note);
    }

    @DeleteMapping(path = "/delete/{noteId}", produces = "application/json")
    public ResponseEntity<Response> deleteNote(@PathVariable long noteId) {
        return mainService.deleteNote(noteId);
    }
}
