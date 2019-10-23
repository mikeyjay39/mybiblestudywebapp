package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.Note;
import com.mybiblestudywebapp.persistence.NoteDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/17/19
 */
public class NoteUnitTests {

    private Note note;
    private NoteDao noteDao;

    @Before
    public void setUp() throws Exception {
        note = null;
        noteDao = new NoteDao(new JdbcTemplate(DbConnectionTest.rebuildEmbeddedDataBase()));
    }

    @After
    public void tearDown() {
        note = null;
    }

    public NoteUnitTests() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DbConnectionTest.getEmbeddedPostgres());
        noteDao = new NoteDao(jdbcTemplate);
    }

    @Test
    public void testSave() throws Exception {
        saveNote();
    }

    @Test
    public void testUpdate() {
        Note retrievedNote = saveAndGet();
        note.setNote("The note has been modified!");
        boolean result = noteDao.update(note);
        Note updatedNote = noteDao.get(note.getNoteId()).get();
        Assert.assertTrue(result);
        Assert.assertEquals("The note has been modified!", updatedNote.getNote());
    }

    @Test
    public void testDelete() {
        note = saveAndGet();
        boolean result = noteDao.delete(note);
        Assert.assertTrue(result);
        Optional<Note> retrievedNote = noteDao.get(1);
        Assert.assertTrue(retrievedNote.isEmpty());
    }

    private Note saveAndGet() {
        note = saveNote();
        note.setNoteId(1);
        note = noteDao.get(1l).get();
        Assert.assertEquals("This is the first note", note.getNote());
        return note;
    }


    private Note saveNote() {
        note = new Note();
        note.setUserId(1);
        note.setBookId(1);
        note.setChapterId(1);
        note.setNote("A new note!");
        note.setPriv(false);
        note.setLang("en");
        long result = noteDao.save(note);
        Assert.assertTrue(result > -1);
        return note;
    }
}
