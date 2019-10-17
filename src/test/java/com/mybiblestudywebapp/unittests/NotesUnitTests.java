package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.Note;
import com.mybiblestudywebapp.persistence.NoteDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/17/19
 */
public class NotesUnitTests {

    private Note note;
    private NoteDao noteDao;

    @Before
    public void setUp() {
        note = null;
    }

    @After
    public void tearDown() {
        note = null;
    }

    public NotesUnitTests() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DbConnectionTest.getEmbeddedPostgres());
        noteDao = new NoteDao(jdbcTemplate);
    }

    @Test
    public void testSave() throws Exception {
        saveNote();
    }

    @Test
    public void testUpdate() throws Exception {
        note = saveNote();
        note.setNoteId(1);
        Note retrievedNote = noteDao.get(1l).get();
        note.setNote("The note has been modified!");
        boolean result = noteDao.update(note);
        Note updatedNote = noteDao.get(note.getNoteId()).get();
        Assert.assertTrue(result);
        Assert.assertEquals("The note has been modified!", updatedNote.getNote());
    }

    private Note saveNote() {
        note = new Note();
        note.setUserId(1);
        note.setBookId(1);
        note.setChapterId(1);
        note.setNote("A new note!");
        note.setPriv(false);
        note.setLang("en");
        boolean result = noteDao.save(note);
        Assert.assertTrue(result);
        return note;
    }
}
