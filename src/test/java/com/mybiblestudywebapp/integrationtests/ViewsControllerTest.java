package com.mybiblestudywebapp.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybiblestudywebapp.dashboard.views.AddViewResponse;
import com.mybiblestudywebapp.dashboard.views.GetViewsResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 11/2/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "admin@admin.com", password = "12345", roles = "USER")
public class ViewsControllerTest {

    @Autowired
    private MockMvc mvc;

    private MockHttpSession session = new MockHttpSession();

    @Test
    @Rollback
    public void addView() throws Exception {


        mvc.perform(get("/login")//.session(session)
                .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andDo(
                        result ->
                        {
                            MvcResult endResult = mvc.perform(post("/views/add")
                                    .with(csrf().asHeader()))
                                    .andExpect(status().isOk())
                                    .andReturn();
                            String response = endResult.getResponse().getContentAsString();
                            ObjectMapper mapper = new ObjectMapper();
                            AddViewResponse addViewResponse = mapper.readValue(response, AddViewResponse.class);
                            Assert.assertTrue(addViewResponse.getViewId() > 0);
                        }
                )
                .andReturn();
    }

    @Test
    public void getViews() throws Exception {

        mvc.perform(get("/login")
                .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andDo(
                        r ->
                        {
                            MvcResult result = mvc.perform(get("/views/get")
                                    .with(csrf().asHeader()))
                                    .andExpect(status().isOk()).andReturn();

                            String response = result.getResponse().getContentAsString();
                            ObjectMapper mapper = new ObjectMapper();
                            GetViewsResponse getViewsResponse = mapper.readValue(response, GetViewsResponse.class);
                            Assert.assertTrue(!getViewsResponse.getViewCodes().isEmpty());
                        }

                )
                .andReturn();
    }
}
