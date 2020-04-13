package com.mybiblestudywebapp.dashboard.users;

import com.mybiblestudywebapp.main.MainService;
import com.mybiblestudywebapp.utils.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

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
    public ResponseEntity<Response> login(
            @RequestHeader Map<String, String> headers,HttpSession session) {
        return mainService.login(headers, session);
    }




}
