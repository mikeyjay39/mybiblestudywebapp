package com.mybiblestudywebapp.persistence;

import com.mybiblestudywebapp.persistence.model.Note;
import com.mybiblestudywebapp.persistence.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/19/19
 */
public interface DaoService {

    /**
     * Add all notes from a user to a view
     * @param userId
     * @param viewId
     * @return
     */
    CompletableFuture<Long> addUserNotesToView(long userId, long viewId);

    /**
     *
     * @param viewCode
     * @param book
     * @param chapterNo
     * @return
     */
    CompletableFuture<List<Note>> getStudyNotesForChapter(String viewCode, String book, long chapterNo)
    throws DaoServiceException;

    /**
     * Creates a new user account. Hashes the password if encoder is present.
     * @param user
     * @return
     */
    CompletableFuture<User> createUserAccount(User user) throws DaoServiceException;

    /**
     * Add a new note
     * @param request
     * @return noteId
     */
    CompletableFuture<Long> addNote(Note request) throws DaoServiceException;

    JdbcTemplate getJdbcTemplate();
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);
    NamedParameterJdbcTemplate getNamedParameterJdbcTemplate();
    void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate);

    }
