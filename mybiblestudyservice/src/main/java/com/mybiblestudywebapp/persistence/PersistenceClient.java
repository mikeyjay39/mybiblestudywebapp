package com.mybiblestudywebapp.persistence;

import com.mybiblestudywebapp.feign.OAuth2FeignAutoConfiguration;
import com.mybiblestudywebapp.utils.http.*;
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
@FeignClient(contextId = "persistenceClient", name = "zuulservice", configuration = OAuth2FeignAutoConfiguration.class)
public interface PersistenceClient {

    @PostMapping("/api/persistence/persistence/addUserNotesToView/{userId}/{viewId}")
    Long addUserNotesToView(@RequestHeader("Authorization") String authHeader, @PathVariable long userId, @PathVariable long viewId);

    @GetMapping("/api/persistence/persistence/getStudyNotesForChapter/{viewCode}/{book}/{chapterNo}")
    List<Note> getStudyNotesForChapter(@RequestHeader("Authorization") String authHeader, @PathVariable String viewCode,
                                              @PathVariable String book,
                                              @PathVariable long chapterNo);

    @PostMapping(
            path = "/api/persistence/persistence/addNote",
            consumes = "application/json",
            produces = "application/json")
    long addNote(@RequestHeader("Authorization") String authHeader, @RequestBody Note note);

    @PostMapping(
            path = "/api/persistence/persistence/rankNote",
            consumes = "application/json",
            produces = "application/json")
    RankNoteResponse rankNote(@RequestHeader("Authorization") String authHeader, @RequestBody RankNoteRequest request);

    @PostMapping("/api/persistence/persistence/login/{username}")
    LoginResponse login(@PathVariable String username);

    @PostMapping("/api/persistence/persistence/addView")
    long addView(@RequestHeader("Authorization") String authHeader);

    @GetMapping("/api/persistence/persistence/getChapter/{book}/{chapterNo}")
    Map<String, Integer> getChapter(@RequestHeader("Authorization") String authHeader, @PathVariable String book,
                                           @PathVariable int chapterNo);

    @GetMapping("/api/persistence/persistence/getViews")
    List<String> getViews(@RequestHeader("Authorization") String authHeader);

    @DeleteMapping("/api/persistence/persistence/removeNoteFromView/{viewcode}/{noteId}")
    String removeNoteFromView(@RequestHeader("Authorization") String authHeader, @PathVariable String viewcode,
                                     @PathVariable long noteId);

    @DeleteMapping("/api/persistence/persistence/deleteView/{viewcode}")
    String deleteView(@RequestHeader("Authorization") String authHeader, @PathVariable String viewcode);

    @GetMapping("/api/persistence/persistence/getUsers")
    List<User> getUsers(@RequestHeader("Authorization") String authHeader);

    @PostMapping("/api/persistence/persistence/addNotesToView/{viewcode}/{authorId}/{ranking}")
    String addNotesToView(@RequestHeader("Authorization") String authHeader, @PathVariable String viewcode,
                                 @PathVariable long authorId,
                                 @PathVariable int ranking);

    @GetMapping("/api/persistence/persistence/getAllChapterNotesForUser/{book}/{chapterNo}/{userId}")
    List<Note> getAllChapterNotesForUser(@RequestHeader("Authorization") String authHeader, @PathVariable String book,
                                                @PathVariable long chapterNo,
                                                @PathVariable long userId);

    @PutMapping(
            path = "/api/persistence/persistence/updateNote",
            consumes = "application/json",
            produces = "application/json")
    String updateNote(@RequestHeader("Authorization") String authHeader, @RequestBody Note note);

    @DeleteMapping("/api/persistence/persistence/deleteNote/{noteId}")
    String deleteNote(@RequestHeader("Authorization") String authHeader, @PathVariable long noteId);

    @GetMapping("/api/persistence/persistence/getComments/{noteId}")
    List<Comment> getComments(@RequestHeader("Authorization") String authHeader, @PathVariable long noteId);

    @PostMapping(
            path = "/api/persistence/persistence/addComment",
            consumes = "application/json",
            produces = "application/json")
    Response addComment(@RequestHeader("Authorization") String authHeader, @RequestBody Comment comment);

    @PostMapping("/api/persistence/persistence/addNoteToView/{viewcode}/{noteId}")
    Response addNoteToView(@RequestHeader("Authorization") String authHeader, @PathVariable String viewcode,
                                  @PathVariable long noteId);
}
