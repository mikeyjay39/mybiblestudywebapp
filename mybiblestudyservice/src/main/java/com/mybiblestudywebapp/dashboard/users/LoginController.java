package com.mybiblestudywebapp.dashboard.users;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.main.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/31/19
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private MainService mainService;

    /**
     * login endpoint
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Response> login() {
        return mainService.login();
    }

}
