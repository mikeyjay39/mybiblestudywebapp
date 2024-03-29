package com.mybiblestudywebapp.bible;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.utils.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/22/19
 */
//@CrossOrigin(origins = "*", allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
@RestController
@RequestMapping("biblestudy")
public class BibleStudyController {

    @Autowired
    private MainService mainService;

    /**
     * Accepts a POST request with json body
     *
     * @param request
     * @return
     */
    @PostMapping
    public ResponseEntity<Response> getChapterAndNotes(
            @RequestBody BibleStudyRequest request) {
        return mainService.getChapterAndNotes(request);
    }

    /**
     * Accepts a GET request with arguments in the url path
     *
     * @param viewCode
     * @param book
     * @param chapterNo
     * @return
     */
    @GetMapping("/{viewCode}/{book}/{chapterNo}")
    public ResponseEntity<Response> getChapterAndNotes(
            @PathVariable String viewCode, @PathVariable String book, @PathVariable int chapterNo) {
        BibleStudyRequest request = new BibleStudyRequest();
        request.setViewCode(viewCode);
        request.setBook(book);
        request.setChapterNo(chapterNo);
        return mainService.getChapterAndNotes(request);
    }

    /**
     * Get only the text of a chapter.
     *
     * @param book
     * @param chapterNo
     * @return response that contains the text and book_id, and chapter_id
     */
    @GetMapping("/{book}/{chapterNo}")
    public ResponseEntity<Response> getChapter(
            @PathVariable String book, @PathVariable int chapterNo) {
        return mainService.getChapter(book, chapterNo);
    }
}
