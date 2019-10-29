package com.mybiblestudywebapp.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybiblestudywebapp.dashboard.notes.AddNoteResponse;
import com.mybiblestudywebapp.persistence.model.Note;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/29/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@Transactional
public class NotesControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @Rollback
    @WithMockUser(username = "admin@admin.com", password = "12345", roles = "USER")
    public void addNote() throws Exception {
        Note note = new Note();
        note.setNoteText("This is a unit test note");
        note.setUserId(1);
        note.setBookId(1);
        note.setChapterId(1);
        note.setVerse(2);
        note.setLang("en");
        note.setPriv(false);
        ObjectMapper mapper = new ObjectMapper();
        String request = mapper.writeValueAsString(note);

        MvcResult result = mvc.perform(post("/notes/add")
        .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        AddNoteResponse responseNote = mapper.readValue(response, AddNoteResponse.class);
        Assert.assertTrue(responseNote.getNoteId() > 0);
    }
}