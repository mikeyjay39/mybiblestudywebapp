package com.mybiblestudywebapp.persistence;

import com.mybiblestudywebapp.utils.http.RankNoteRequest;
import com.mybiblestudywebapp.utils.http.RankNoteResponse;
import com.mybiblestudywebapp.utils.http.Response;
import com.mybiblestudywebapp.utils.persistence.model.Comment;
import com.mybiblestudywebapp.utils.persistence.model.Note;
import com.mybiblestudywebapp.utils.persistence.model.User;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 3/22/20
 */
@Service
@DefaultProperties(
        threadPoolKey = "persistenceThreadPool",
        commandProperties=
                {@HystrixProperty(
                        name="execution.isolation.thread.timeoutInMilliseconds",
                        value="5000")})
public class PersistenceService {

    private final PersistenceClient persistenceClient;

    private static Logger logger = LoggerFactory.getLogger(PersistenceService.class);

    @Autowired
    public PersistenceService(PersistenceClient persistenceClient) {
        this.persistenceClient = persistenceClient;
    }

    @HystrixCommand
    public Long addUserNotesToView(long userId, long viewId) {
        return persistenceClient.addUserNotesToView(userId, viewId);
    }

    @HystrixCommand
    public List<Note> getStudyNotesForChapter( String viewCode,
                                               String book,
                                               long chapterNo) {
        return persistenceClient.getStudyNotesForChapter(viewCode, book, chapterNo);
    }
    
    @HystrixCommand
    public long addNote( Note note) {
        return persistenceClient.addNote(note);
    }
    
    @HystrixCommand
    public RankNoteResponse rankNote( RankNoteRequest request) {
        return persistenceClient.rankNote(request);
    }
    
    @HystrixCommand
    public long addView() {
        return persistenceClient.addView();
    }
    
    @HystrixCommand
    public Map<String, Integer> getChapter( String book,
                                            int chapterNo) {
        return persistenceClient.getChapter(book, chapterNo);
    }
    
    @HystrixCommand
    public List<String> getViews() {
        return persistenceClient.getViews();
    }

    @HystrixCommand
    public String removeNoteFromView( String viewcode,
                                      long noteId) {
        return persistenceClient.removeNoteFromView(viewcode, noteId);
    }

    @HystrixCommand
    public String deleteView( String viewcode) {
        return persistenceClient.deleteView(viewcode);
    }

    @HystrixCommand
    public List<User> getUsers() {
        return persistenceClient.getUsers();
    }

    @HystrixCommand
    public String addNotesToView( String viewcode,
                                  long authorId,
                                  int ranking) {
        return persistenceClient.addNotesToView(viewcode, authorId, ranking);
    }

    @HystrixCommand
    public List<Note> getAllChapterNotesForUser( String book,
                                                 long chapterNo,
                                                 long userId) {
        return persistenceClient.getAllChapterNotesForUser(book, chapterNo, userId);
    }

    @HystrixCommand
    public String updateNote( Note note) {
        return persistenceClient.updateNote(note);
    }

    @HystrixCommand
    public String deleteNote( long noteId) {
        return persistenceClient.deleteNote(noteId);
    }

    @HystrixCommand
    public List<Comment> getComments( long noteId) {
        return persistenceClient.getComments(noteId);
    }

    @HystrixCommand
    public Response addComment( Comment comment) {
        return persistenceClient.addComment(comment);
    }

    @HystrixCommand
    public Response addNoteToView( String viewcode,
                                   long noteId) {
        return persistenceClient.addNoteToView(viewcode, noteId);
    }
}
