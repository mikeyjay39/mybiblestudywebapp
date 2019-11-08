package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.bible.BibleStudyRequest;
import com.mybiblestudywebapp.dashboard.notes.RankNoteRequest;
import com.mybiblestudywebapp.dashboard.users.CreateUserRequest;
import com.mybiblestudywebapp.persistence.model.Note;
import org.springframework.http.ResponseEntity;

/**
 * Acts as a boss thread delegating requests to async worker services and packages responses into Response Entities
 * to return to the controllers.
 *
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/21/19
 */
public interface MainService {

    /**
     * Get verses and notes for a chapter and view
     * @param request
     * @return
     */
    ResponseEntity<Response> getChapterAndNotes(BibleStudyRequest request);

    /**
     * Create a new user account
     * @param request
     * @return
     */
    ResponseEntity<Response> createUserAccount(CreateUserRequest request);

    /**
     * Add a new note
     * @param request
     * @return
     */
    ResponseEntity<Response> addNote(Note request);

    /**
     * Ranks a note. increaseRanking field should be set to true if we are increasing the ranking
     * otherwise it decrements it.
     * @param request
     * @return
     */
    ResponseEntity<Response> rankNote(RankNoteRequest request);

    /**
     * Processes a login request. Will initialize a user session bean with the user_id
     * @return
     */
    ResponseEntity<Response> login();

    /**
     * Creates a new view to hold notes.
     * @return
     */
    ResponseEntity<Response> addView();

    /**
     * Get only the text of a chapter.
     * @param book
     * @param chapterNo
     * @return response that contains the text and book_id, and chapter_id
     */
    ResponseEntity<Response> getChapter(String book, int chapterNo);

    /**
     * Get view codes for currently logged in user.
     * @return
     */
    ResponseEntity<Response> getViews();

    /**
     * Remove a note from a view
     * @param viewcode
     * @param noteId
     * @return
     */
    ResponseEntity<String> removeNoteFromView(String viewcode, long noteId);

    /**
     * Deletes a View based on viewcode
     * @param viewcode
     * @return response success or failure
     */
    ResponseEntity<Response> deleteView(String viewcode);

    /**
     * Get a list of all the users. Response contains their whole name and their user_id.
     * @return
     */
    ResponseEntity<Response> getUsers();

    /**
     * Add all notes from an author above a certain ranking to the target viewcode.
     * @param viewcode target viewcode
     * @param authorId author's notes to add
     * @param ranking all notes above this ranking
     * @return
     */
    ResponseEntity<Response> addNotesToView(String viewcode, long authorId, int ranking);

    /**
     * Get all the chapter notes for the logged in user
     * @param book
     * @param chapterNo
     * @param userId
     * @return
     */
    ResponseEntity<Response> getChapterNotesForUser(String book, int chapterNo, long userId);

    /**
     * Updates a note owned by the logged in user
     * @param note
     * @return
     */
    ResponseEntity<Response> updateNote(Note note);

    /**
     * Delete a note.
     * @param noteId
     * @return
     */
    ResponseEntity<Response> deleteNote(long noteId);

    /**
     * Get comments for a note
     * @param noteId
     * @return
     */
    ResponseEntity<Response> getComments(long noteId);
}
