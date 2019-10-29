package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.client.BibleStudyRequest;
import com.mybiblestudywebapp.dashboard.users.CreateUserRequest;
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

    ResponseEntity<Response> createUserAccount(CreateUserRequest request);

}
