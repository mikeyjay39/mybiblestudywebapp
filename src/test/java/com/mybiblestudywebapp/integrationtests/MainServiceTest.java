package com.mybiblestudywebapp.integrationtests;

import com.mybiblestudywebapp.dashboard.users.CreateUserRequest;
import com.mybiblestudywebapp.dashboard.users.CreateUserResponse;
import com.mybiblestudywebapp.main.MainService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/27/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MainServiceTest {

    @Autowired
    private MainService mainService;

    @Rollback
    @Test
    public void createUserAccount() throws Exception {
        CreateUserRequest user = new CreateUserRequest();
        String password = "testpass";
        user.setPassword(password);
        user.setFirstname("Main Service");
        user.setLastname("Integration Test");
        user.setEmail("mainservice@intergation.test");
        var result = mainService.createUserAccount(user);
        CreateUserResponse body = (CreateUserResponse) result.getBody();
        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assert.assertNotNull(body.getUserId());
    }
}
