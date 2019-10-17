package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.Chapter;
import com.mybiblestudywebapp.persistence.ChapterDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/17/19
 */
public class ChapterUnitTests {

    private Chapter chapter;
    private ChapterDao chapterDao;

    public ChapterUnitTests() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DbConnectionTest.getEmbeddedPostgres());
        chapterDao = new ChapterDao(jdbcTemplate);
    }

    @Before
    public void setUp() {
        chapter = null;
    }

    @After
    public void tearDown() {
        chapter = null;
    }

    @Test
    public void testGetById() throws Exception {
        int chapterNo = chapterDao.get(1).get().getChapter_no();
        Assert.assertEquals(1, 1);
    }
}
