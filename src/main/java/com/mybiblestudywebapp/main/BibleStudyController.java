package com.mybiblestudywebapp.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/22/19
 */
@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class BibleStudyController {

    @Autowired
    private MainService mainService;

    @PostMapping(path = "/biblestudy", consumes = "application/json", produces = "application/json")
    public ResponseEntity<BibleStudyResponse> getChapterAndNotes(
            @RequestBody BibleStudyRequest request) {
        return mainService.getChapterAndNotes(request);
    }
}
