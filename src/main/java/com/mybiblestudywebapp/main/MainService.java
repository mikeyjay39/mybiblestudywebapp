package com.mybiblestudywebapp.main;

import com.mybiblestudywebapp.persistence.model.User;
import org.springframework.http.ResponseEntity;

/**
 * Delegates requests to async services and packages responses into Response Entities to
 * return to the controllers.
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
    ResponseEntity<BibleStudyResponse> getChapterAndNotes(BibleStudyRequest request);

    ResponseEntity<CreateUserAccountResponse> createUserAccount(User request);

}
