package com.mybiblestudywebapp.dashboard.users;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.main.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/27/19
 */
@RestController
@RequestMapping("users")
public class UsersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private MainService mainService;

    @RequestMapping("/signup")
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<? extends Response> create(
            @RequestBody CreateUserRequest request) {
        return mainService.createUserAccount(request);
    }
}
