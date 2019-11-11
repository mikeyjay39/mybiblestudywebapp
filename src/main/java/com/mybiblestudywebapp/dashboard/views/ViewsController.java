package com.mybiblestudywebapp.dashboard.views;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.main.Response;
import com.mybiblestudywebapp.persistence.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/2/19
 */
@RestController
@RequestMapping("views")
public class ViewsController {

    @Autowired
    private MainService mainService;

    @PostMapping(path = "/add")
    public ResponseEntity<Response> addView() {
        return mainService.addView();
    }

    @GetMapping(path = "/get")
    public ResponseEntity<Response> getViews() {
        return mainService.getViews();
    }

    /**
     * Endpoint to add all notes from an author above a certain ranking to the target viewcode.
     * @param viewcode target viewcode
     * @param author author's notes to add
     * @param ranking all notes above this ranking
     * @return
     */
    @PostMapping(path = "/add/{viewcode}/{author}/{ranking}")
    public ResponseEntity<Response> addNotesToView(@PathVariable String viewcode,
                                                   @PathVariable long author,
                                                   @PathVariable int ranking) {
        return mainService.addNotesToView(viewcode, author, ranking);
    }

    /**
     * Add a single note to a view
     * @param viewcode
     * @param noteId
     * @return
     */
    @PostMapping(path = "/add/{viewcode}/{noteId}")
    public ResponseEntity<Response> addNoteToView(@PathVariable String viewcode,
                                                  @PathVariable long noteId) {
        return mainService.addNoteToView(viewcode, noteId);
    }

    @DeleteMapping("/{viewcode}/{noteId}")
    public ResponseEntity<String> removeNoteFromView(
            @PathVariable String viewcode, @PathVariable long noteId) {
        return mainService.removeNoteFromView(viewcode, noteId);
    }

    @DeleteMapping("/{viewcode}/delete")
    public ResponseEntity<Response> deleteView(@PathVariable String viewcode) {
        return mainService.deleteView(viewcode);
    }

}
