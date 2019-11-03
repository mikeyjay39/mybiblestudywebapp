package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.client.BibleStudyRequest;
import com.mybiblestudywebapp.dashboard.notes.RankNoteRequest;
import com.mybiblestudywebapp.dashboard.users.CreateUserRequest;
import com.mybiblestudywebapp.persistence.model.Note;
import com.mybiblestudywebapp.persistence.model.View;
import org.springframework.http.ResponseEntity;

import java.security.Principal;

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
     * @param view
     * @return
     */
    ResponseEntity<Response> addView(View view);

}
