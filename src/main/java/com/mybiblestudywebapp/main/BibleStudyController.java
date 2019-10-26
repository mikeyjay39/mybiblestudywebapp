package com.mybiblestudywebapp.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/22/19
 */
@CrossOrigin(origins = "*", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RestController
@RequestMapping("biblestudy")
public class BibleStudyController {

    @Autowired
    private MainService mainService;

    /**
     * Accepts a POST request with json body
     * @param request
     * @return
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<BibleStudyResponse> getChapterAndNotes(
            @RequestBody BibleStudyRequest request) {
        return mainService.getChapterAndNotes(request);
    }

    /**
     * Accepts a GET request with arguments in the url path
     * @param viewCode
     * @param book
     * @param chapterNo
     * @return
     */
    @GetMapping("/{viewCode}/{book}/{chapterNo}")
    public ResponseEntity<BibleStudyResponse> getChapterAndNotes(
            @PathVariable String viewCode, @PathVariable String book, @PathVariable int chapterNo) {
        BibleStudyRequest request = new BibleStudyRequest();
        request.setViewCode(viewCode);
        request.setBook(book);
        request.setChapterNo(chapterNo);
        return mainService.getChapterAndNotes(request);
    }
}
