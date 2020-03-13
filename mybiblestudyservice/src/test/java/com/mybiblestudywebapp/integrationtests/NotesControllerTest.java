package com.mybiblestudywebapp.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mybiblestudywebapp.bible.BibleStudyResponse;
import com.mybiblestudywebapp.dashboard.notes.AddNoteResponse;
import com.mybiblestudywebapp.dashboard.notes.GetCommentsResponse;
import com.mybiblestudywebapp.dashboard.notes.RankNoteRequest;
import com.mybiblestudywebapp.dashboard.notes.RankNoteResponse;
import com.mybiblestudywebapp.dashboard.views.GetViewsResponse;
import com.mybiblestudywebapp.main.GenericResponse;
import com.mybiblestudywebapp.persistence.model.Comment;
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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
@WithMockUser(username = "admin@admin.com", password = "12345", roles = "USER")
public class NotesControllerTest {

    @Autowired
    private MockMvc mvc;

    private ObjectMapper mapper = new ObjectMapper();

    public NotesControllerTest() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @Rollback
    public void addNote() throws Exception {
        Note note = new Note();
        note.setNoteText("This is a unit test note");
        note.setUserId(1);
        note.setBookId(1);
        note.setChapterId(1);
        note.setVerseStart(2);
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

    @Rollback
    @Test
    public void rankNote() throws Exception {
        RankNoteRequest request = new RankNoteRequest();
        request.setNoteId(1);
        request.setUserId(2);
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(request);

        MvcResult mvcResult = mvc.perform(post("/notes/rank")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        RankNoteResponse response = mapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                RankNoteResponse.class
        );

        Assert.assertEquals("success", response.getResult());
    }

    @Test
    public void getChapterNotesForUser() throws Exception {

        MvcResult result = mvc.perform(get("/notes/mynotes/Genesis/1/1")
                .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @Rollback
    public void updateNote() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Note note = new Note();
        String newNoteText = "This note has been updated";
        note.setNoteId(1);
        note.setBookId(1);
        note.setChapterId(1);
        note.setVerseStart(2);
        note.setLang("en");
        note.setPriv(false);
        note.setNoteText(newNoteText);
        String jsonRequest = mapper.writeValueAsString(note);

        mvc.perform(put("/notes/update")
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andDo(
                        r ->
                        {
                            MvcResult result = mvc.perform(get("/notes/mynotes/Genesis/1/1")
                                    .with(csrf().asHeader()))
                                    .andExpect(status().isOk()).andReturn();

                            String response = result.getResponse().getContentAsString();
                            BibleStudyResponse getViewsResponse = mapper.readValue(response, BibleStudyResponse.class);

                            // iterate through notes and look for our updated note
                            for (Note n : getViewsResponse.getNotes()) {

                                if (n.getNoteId() == 1) {
                                    Assert.assertEquals(newNoteText, n.getNoteText());
                                }
                            }
                        }
                )
                .andReturn();
    }

    @Test
    @Rollback
    public void deleteNote() throws Exception {

        mvc.perform(get("/login")
                .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andDo(
                        r ->
                                mvc.perform(delete("/notes/delete/1")
                                        .with(csrf().asHeader()))
                                        .andExpect(status().isOk()).andReturn());
    }

    @Test
    public void getComments() throws Exception {

        mvc.perform(get("/login")
                .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andDo(
                        r ->
                        {
                            MvcResult result = mvc.perform(get("/notes/comments/1")
                                    .with(csrf().asHeader()))
                                    .andExpect(status().isOk()).andReturn();

                            String stringResponse = result.getResponse().getContentAsString();
                            GetCommentsResponse response =
                                    mapper.readValue(stringResponse, GetCommentsResponse.class);
                            Assert.assertTrue(!response.getComments().isEmpty());
                        }
                );
    }

    @Test
    @Rollback
    public void addComment() throws Exception {

        mvc.perform(get("/login")
                .with(csrf().asHeader()))
                .andExpect(status().isOk())
                .andDo(
                        r ->
                        {
                            Comment comment = new Comment();
                            comment.setNoteId(1);
                            comment.setCommentText("This is a unit test comment");
                            String jsonRequest = mapper.writeValueAsString(comment);

                            MvcResult result = mvc.perform(post("/notes/comments")
                                    .with(csrf().asHeader())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(jsonRequest))
                                    .andExpect(status().isOk())
                                    .andReturn();

                            String stringResponse = result.getResponse().getContentAsString();
                            GenericResponse response =
                                    mapper.readValue(stringResponse, GenericResponse.class);
                            Assert.assertTrue(response.getEntityId() > 0);
                            Assert.assertEquals("success", response.getStatus());
                        });
    }
}
