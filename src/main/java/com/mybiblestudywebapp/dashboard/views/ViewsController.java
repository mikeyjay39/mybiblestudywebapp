package com.mybiblestudywebapp.dashboard.views;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.main.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
