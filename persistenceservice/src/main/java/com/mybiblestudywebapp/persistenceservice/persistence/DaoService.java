package com.mybiblestudywebapp.persistenceservice.persistence;

import com.mybiblestudywebapp.utils.http.*;
import com.mybiblestudywebapp.utils.persistence.DaoServiceException;
import com.mybiblestudywebapp.utils.persistence.model.Comment;
import com.mybiblestudywebapp.utils.persistence.model.Note;
import com.mybiblestudywebapp.utils.persistence.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/19/19
 */
public interface DaoService {

    /**
     * Add all notes from a user to a view
     *
     * @param userId
     * @param viewId
     * @return
     */
    Long addUserNotesToView(long userId, long viewId);

    /**
     * @param viewCode
     * @param book
     * @param chapterNo
     * @return
     */
    List<Note> getStudyNotesForChapter(String viewCode, String book, long chapterNo);

    /**
     * Creates a new user account. Hashes the password if encoder is present.
     *
     * @param user
     * @return
     */
    User createUserAccount(User user) throws DaoServiceException;

    /**
     * Add a new note
     *
     * @param request
     * @return noteId
     */
    long addNote(Note request);

    /**
     * Ranks a note. increaseRanking field should be set to true if we are increasing the ranking
     * otherwise it decrements it.
     *
     * @param request
     * @return
     */
    RankNoteResponse rankNote(RankNoteRequest request);

    LoginResponse login(String username);

    /**
     * Creates a new view to hold notes. Gets the request from MainService and forwards it to the ViewDao
     *
     * @return
     */
    long addView();

    /**
     * Get only the text of a chapter.
     *
     * @param book
     * @param chapterNo
     * @return response that contains the book_id, and chapter_id
     */
    Map<String, Integer> getChapter(String book, int chapterNo);

    /**
     * Get all the view codes for the currently logged in user.
     *
     * @return
     */
    List<String> getViews();

    /**
     * Remove a note from a view
     *
     * @param viewcode
     * @param noteId
     * @return
     */
    String removeNoteFromView(String viewcode, long noteId);

    /**
     * Deletes a View based on viewcode
     *
     * @param viewcode
     * @return response success or failure
     */
    String deleteView(String viewcode);

    /**
     * Returns all users
     *
     * @return
     */
    List<User> getUsers();

    /**
     * Add all notes from an author above a certain ranking to the target viewcode.
     *
     * @param viewcode target viewcode
     * @param authorId author's notes to add
     * @param ranking  all notes above this ranking
     * @return
     */
    String addNotesToView(String viewcode, long authorId, int ranking);

    /**
     * Gets all notes for the logged in user
     *
     * @return
     */
    List<Note> getAllChapterNotesForUser(String book, long chapterNo, long userId);

    /**
     * Updates a note
     *
     * @param note
     * @return "success"  on success
     */
    String updateNote(Note note);

    /**
     * Delete a note. The userId stored in the user session will be
     * used to verify the user is deleting a note that they own.
     *
     * @param noteId
     * @return "success" on success
     */
    String deleteNote(long noteId);

    /**
     * Get comments for a note
     *
     * @param noteId
     * @return
     */
    List<Comment> getComments(long noteId);

    /**
     * Add a comment
     *
     * @param comment
     * @return
     */
    Response addComent(Comment comment);

    /**
     * Add a single note to a view
     *
     * @param viewcode
     * @param noteId
     * @return
     */
    Response addNoteToView(String viewcode, long noteId);
}
