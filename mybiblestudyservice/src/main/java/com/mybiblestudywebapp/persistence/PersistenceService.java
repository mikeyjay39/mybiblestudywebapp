package com.mybiblestudywebapp.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybiblestudywebapp.security.OAuth2Session;
import com.mybiblestudywebapp.utils.http.*;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
        commandProperties =
                {@HystrixProperty(
                        name = "execution.isolation.thread.timeoutInMilliseconds",
                        value = "10000")})
public class PersistenceService {

    private final PersistenceClient persistenceClient;

    private static Logger logger = LoggerFactory.getLogger(PersistenceService.class);

    @Autowired
    private OAuth2Session oAuth2Session;

    @Autowired
    public PersistenceService(PersistenceClient persistenceClient) {
        this.persistenceClient = persistenceClient;
    }

    @HystrixCommand
    public Long addUserNotesToView(long userId, long viewId) {
        return persistenceClient.addUserNotesToView(oAuth2Session.authHeader, userId, viewId);
    }

    @HystrixCommand
    public List<Note> getStudyNotesForChapter(String viewCode,
                                              String book,
                                              long chapterNo) {
        return persistenceClient.getStudyNotesForChapter(oAuth2Session.authHeader, viewCode, book, chapterNo);
    }

    @HystrixCommand
    public LoginResponse login(String username) {
        return persistenceClient.login(username);
    }


    @HystrixCommand
    public long addNote(Note note) {
        return persistenceClient.addNote(oAuth2Session.authHeader, note);
    }

    @HystrixCommand
    public RankNoteResponse rankNote(RankNoteRequest request) {
        return persistenceClient.rankNote(oAuth2Session.authHeader, request);
    }

    @HystrixCommand
    public long addView() {
        return persistenceClient.addView(oAuth2Session.authHeader);
    }

    @HystrixCommand
    public Map<String, Integer> getChapter(String book,
                                           int chapterNo) {
        return persistenceClient.getChapter(oAuth2Session.authHeader, book, chapterNo);
    }

    @HystrixCommand
    public List<String> getViews() {
        return persistenceClient.getViews(oAuth2Session.authHeader);
    }

    @HystrixCommand
    public String removeNoteFromView(String viewcode,
                                     long noteId) {
        return persistenceClient.removeNoteFromView(oAuth2Session.authHeader, viewcode, noteId);
    }

    @HystrixCommand
    public String deleteView(String viewcode) {
        return persistenceClient.deleteView(oAuth2Session.authHeader, viewcode);
    }

    @HystrixCommand
    public List<User> getUsers() {
        return persistenceClient.getUsers(oAuth2Session.authHeader);
    }

    @HystrixCommand
    public String addNotesToView(String viewcode,
                                 long authorId,
                                 int ranking) {
        return persistenceClient.addNotesToView(oAuth2Session.authHeader, viewcode, authorId, ranking);
    }

    @HystrixCommand
    public List<Note> getAllChapterNotesForUser(String book,
                                                long chapterNo,
                                                long userId) {
        return persistenceClient.getAllChapterNotesForUser(oAuth2Session.authHeader, book, chapterNo, userId);
    }

    @HystrixCommand
    public String updateNote(Note note) {
        return persistenceClient.updateNote(oAuth2Session.authHeader, note);
    }

    @HystrixCommand
    public String deleteNote(long noteId) {
        return persistenceClient.deleteNote(oAuth2Session.authHeader, noteId);
    }

    @HystrixCommand
    public List<Comment> getComments(long noteId) {
        return persistenceClient.getComments(oAuth2Session.authHeader, noteId);
    }

    @HystrixCommand
    public Response addComment(Comment comment) {
        return persistenceClient.addComment(oAuth2Session.authHeader, comment);
    }

    @HystrixCommand
    public Response addNoteToView(String viewcode,
                                  long noteId) {
        return persistenceClient.addNoteToView(oAuth2Session.authHeader, viewcode, noteId);
    }
}
