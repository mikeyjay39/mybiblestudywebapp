package com.mybiblestudywebapp.dashboard.notes;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.main.Response;
import com.mybiblestudywebapp.persistence.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/29/19
 */
@RestController
@RequestMapping("notes")
public class NotesController {

    @Autowired
    private MainService mainService;

    /**
     * Endpoint for adding new notes
     * @param request
     * @return
     */
    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> addNote(@RequestBody Note request) {
        return mainService.addNote(request);
    }

    /**
     * Endpoint for ranking notes
     * @param request
     * @return
     */
    @PostMapping(path = "/rank", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> rankNote(@RequestBody RankNoteRequest request) {
        return mainService.rankNote(request);
    }
}
