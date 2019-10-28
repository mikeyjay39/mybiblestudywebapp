package com.mybiblestudywebapp.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybiblestudywebapp.main.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/27/19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsersControllerTest {

    @Autowired
    private MockMvc mvc;

    @Rollback
    @Test
    public void create() throws Exception {
        CreateUserRequest requestObj = new CreateUserRequest();
        requestObj.setEmail("testingemail@testingthis.com");
        requestObj.setFirstname("Abe");
        requestObj.setLastname("Lincoln");
        requestObj.setPassword("testpassword");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(requestObj);

        mvc.perform(post("/users/signup")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk());
    }
}