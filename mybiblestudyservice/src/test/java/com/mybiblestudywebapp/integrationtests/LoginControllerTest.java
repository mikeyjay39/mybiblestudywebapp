package com.mybiblestudywebapp.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybiblestudywebapp.utils.http.LoginResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/31/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WithMockUser(username = "admin@admin.com", password = "12345", roles = "USER")
public class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void login() throws Exception {
        MvcResult result = mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        LoginResponse loginResponse = mapper.readValue(response, LoginResponse.class);
        Assert.assertNotNull(loginResponse.getUserId());
    }
}