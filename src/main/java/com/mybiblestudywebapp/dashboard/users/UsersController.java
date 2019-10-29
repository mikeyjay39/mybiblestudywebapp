package com.mybiblestudywebapp.dashboard.users;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.main.Response;
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


    @Autowired
    private MainService mainService;

    @PostMapping(path = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Response> create(
            @RequestBody CreateUserRequest request) {
        return mainService.createUserAccount(request);
    }
}
