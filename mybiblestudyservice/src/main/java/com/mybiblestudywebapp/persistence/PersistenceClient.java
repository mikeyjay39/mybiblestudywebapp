package com.mybiblestudywebapp.persistence;

import com.mybiblestudywebapp.utils.http.RankNoteRequest;
import com.mybiblestudywebapp.utils.http.RankNoteResponse;
import com.mybiblestudywebapp.utils.http.Response;
import com.mybiblestudywebapp.utils.persistence.model.Comment;
import com.mybiblestudywebapp.utils.persistence.model.Note;
import com.mybiblestudywebapp.utils.persistence.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/22/20
 */
@FeignClient(contextId = "persistenceClient", name = "zuulservice")
public interface PersistenceClient {

    @PostMapping("/api/persistence/persistence/addUserNotesToView/{userId}/{viewId}")
    Long addUserNotesToView(@PathVariable long userId, @PathVariable long viewId);

    @GetMapping("/api/persistence/persistence/getStudyNotesForChapter/{viewCode}/{book}/{chapterNo}")
    List<Note> getStudyNotesForChapter(@PathVariable String viewCode,
                                              @PathVariable String book,
                                              @PathVariable long chapterNo);

    @PostMapping("/api/persistence/persistence/addNote/{note}")
    long addNote(@PathVariable Note note);

    @PostMapping("/api/persistence/persistence/rankNote/{request}")
    RankNoteResponse rankNote(@PathVariable RankNoteRequest request);

    @PostMapping("/api/persistence/persistence/login/{username}")
    Response login(@PathVariable String username);

    @PostMapping("/api/persistence/persistence/addView")
    long addView();

    @GetMapping("/api/persistence/persistence/getChapter/{book}/{chapterNo}")
    Map<String, Integer> getChapter(@PathVariable String book,
                                           @PathVariable int chapterNo);

    @GetMapping("/api/persistence/persistence/getViews")
    List<String> getViews();

    @DeleteMapping("/api/persistence/persistence/removeNoteFromView/{viewcode}/{noteId}")
    String removeNoteFromView(@PathVariable String viewcode,
                                     @PathVariable long noteId);

    @DeleteMapping("/api/persistence/persistence/deleteView/{viewcode}")
    String deleteView(@PathVariable String viewcode);

    @GetMapping("/api/persistence/persistence/getUsers")
    List<User> getUsers();

    @PostMapping("/api/persistence/persistence/addNotesToView/{viewcode}/{authorId}/{ranking}")
    String addNotesToView(@PathVariable String viewcode,
                                 @PathVariable long authorId,
                                 @PathVariable int ranking);

    @GetMapping("/api/persistence/persistence/getAllChapterNotesForUser/{book}/{chapterNo}/{userId}")
    List<Note> getAllChapterNotesForUser(@PathVariable String book,
                                                @PathVariable long chapterNo,
                                                @PathVariable long userId);

    @PutMapping("/api/persistence/persistence/updateNote/{note}")
    String updateNote(@PathVariable Note note);

    @DeleteMapping("/api/persistence/persistence/deleteNote/{noteId}")
    String deleteNote(@PathVariable long noteId);

    @GetMapping("/api/persistence/persistence/getComments/{noteId}")
    List<Comment> getComments(@PathVariable long noteId);

    @PostMapping("/api/persistence/persistence/addComment/{comment}")
    Response addComment(@PathVariable Comment comment);

    @PostMapping("/api/persistence/persistence/addNoteToView/{viewcode}/{noteId}")
    Response addNoteToView(@PathVariable String viewcode,
                                  @PathVariable long noteId);
}
