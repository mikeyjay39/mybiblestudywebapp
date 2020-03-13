package com.mybiblestudywebapp.dashboard.users;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.main.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Use this to get a CSRF token before calling createAccount
     *
     * @return
     */
    @PostMapping(path = "/csrf/{get}")
    public ResponseEntity<String> getCsrfToken(@RequestParam String get) {
        return ResponseEntity.ok("csrf sent");
    }

    @GetMapping
    public ResponseEntity<Response> getUsers() {
        return mainService.getUsers();
    }

}
