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
@FeignClient("zuulservice")
public interface PersistenceClient {

    @PostMapping("/api/addUserNotesToView/{userId}/{viewId}")
    Long addUserNotesToView(@PathVariable long userId, @PathVariable long viewId);

    @GetMapping("/api/getStudyNotesForChapter/{viewCode}/{book}/{chapterNo}")
    List<Note> getStudyNotesForChapter(@PathVariable String viewCode,
                                              @PathVariable String book,
                                              @PathVariable long chapterNo);

    @PostMapping("/api/addNote/{note}")
    long addNote(@PathVariable Note note);

    @PostMapping("/api/rankdNote/{request}")
    RankNoteResponse rankNote(@PathVariable RankNoteRequest request);

    @PostMapping("/api/addView")
    long addView();

    @GetMapping("/api/getChapter/{book}/{chapterNo}")
    Map<String, Integer> getChapter(@PathVariable String book,
                                           @PathVariable int chapterNo);

    @GetMapping("/api/getViews")
    List<String> getViews();

    @DeleteMapping("/api/removeNoteFromView/{viewcode}/{noteId}")
    String removeNoteFromView(@PathVariable String viewcode,
                                     @PathVariable long noteId);

    @DeleteMapping("/api/deleteView/{viewcode}")
    String deleteView(@PathVariable String viewcode);

    @GetMapping("/api/getUsers")
    List<User> getUsers();

    @PostMapping("/api/addNotesToView/{viewcode}/{authorId}/{ranking}")
    String addNotesToView(@PathVariable String viewcode,
                                 @PathVariable long authorId,
                                 @PathVariable int ranking);

    @GetMapping("/api/getAllChapterNotesForUser/{book}/{chapterNo}/{userId}")
    List<Note> getAllChapterNotesForUser(@PathVariable String book,
                                                @PathVariable long chapterNo,
                                                @PathVariable long userId);

    @PutMapping("/api/updateNote/{note}")
    String updateNote(@PathVariable Note note);

    @DeleteMapping("/api/deleteNote/{noteId}")
    String deleteNote(@PathVariable long noteId);

    @GetMapping("/api/getComments/{noteId}")
    List<Comment> getComments(@PathVariable long noteId);

    @PostMapping("/api/addComment/{comment}")
    Response addComment(@PathVariable Comment comment);

    @PostMapping("/api/addNoteToView/{viewcode}/{noteId}")
    Response addNoteToView(@PathVariable String viewcode,
                                  @PathVariable long noteId);
}
