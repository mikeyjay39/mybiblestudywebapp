package com.mybiblestudywebapp.persistence;

import com.mybiblestudywebapp.dashboard.notes.RankNoteRequest;
import com.mybiblestudywebapp.dashboard.notes.RankNoteResponse;
import com.mybiblestudywebapp.main.Response;
import com.mybiblestudywebapp.persistence.model.Note;
import com.mybiblestudywebapp.persistence.model.User;
import com.mybiblestudywebapp.persistence.model.View;

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
     * @param view
     * @return
     */
    CompletableFuture<Long> addView(View view) throws DaoServiceException;

}
