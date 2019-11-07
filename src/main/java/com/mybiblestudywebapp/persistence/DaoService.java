package com.mybiblestudywebapp.persistence;

import com.mybiblestudywebapp.bible.GetChapterResponse;
import com.mybiblestudywebapp.dashboard.notes.RankNoteRequest;
import com.mybiblestudywebapp.dashboard.notes.RankNoteResponse;
import com.mybiblestudywebapp.dashboard.views.AddNotesToViewResponse;
import com.mybiblestudywebapp.main.Response;
import com.mybiblestudywebapp.persistence.model.Note;
import com.mybiblestudywebapp.persistence.model.User;
import com.mybiblestudywebapp.persistence.model.View;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
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

    /**
     * Ranks a note. increaseRanking field should be set to true if we are increasing the ranking
     * otherwise it decrements it.
     * @param request
     * @return
     */
    CompletableFuture<RankNoteResponse> rankNote(RankNoteRequest request) throws DaoServiceException;

    Response login(String username);

    /**
     * Creates a new view to hold notes. Gets the request from MainService and forwards it to the ViewDao
     * @return
     */
    CompletableFuture<Long> addView() throws DaoServiceException;

    /**
     * Get only the text of a chapter.
     * @param book
     * @param chapterNo
     * @return response that contains the book_id, and chapter_id
     */
    CompletableFuture<Map<String, Integer>> getChapter(String book, int chapterNo) throws DaoServiceException;

    /**
     * Get all the view codes for the currently logged in user.
     * @return
     * @throws DaoServiceException
     */
    List<String> getViews() throws DaoServiceException;

    /**
     * Remove a note from a view
     * @param viewcode
     * @param noteId
     * @return
     * @throws DaoServiceException
     */
    String removeNoteFromView(String viewcode, long noteId);

    /**
     * Deletes a View based on viewcode
     * @param viewcode
     * @return response success or failure
     */
    CompletableFuture<String> deleteView(String viewcode) throws DaoServiceException;

    /**
     * Returns all users
     * @return
     * @throws DaoServiceException
     */
    CompletableFuture<List<User>> getUsers() throws DaoServiceException;

    /**
     * Add all notes from an author above a certain ranking to the target viewcode.
     * @param viewcode target viewcode
     * @param authorId author's notes to add
     * @param ranking all notes above this ranking
     * @return
     */
    CompletableFuture<String> addNotesToView(String viewcode, long authorId, int ranking)
        throws DaoServiceException;
}
