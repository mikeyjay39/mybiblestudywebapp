package com.mybiblestudywebapp.main;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/27/19
 */
@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> testing() {
        return ResponseEntity.ok("It works");
    }
}
