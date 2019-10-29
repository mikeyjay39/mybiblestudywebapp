package com.mybiblestudywebapp.dashboard.notes;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.main.Response;
import com.mybiblestudywebapp.persistence.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(path = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> addNote(@RequestBody Note request) {
        return mainService.addNote(request);
    }
}
