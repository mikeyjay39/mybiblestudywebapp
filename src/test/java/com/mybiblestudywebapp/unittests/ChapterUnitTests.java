package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.Chapter;
import com.mybiblestudywebapp.persistence.ChapterDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.TreeMap;

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
        insertChpaters();

        // get first chapter in Genesis
        chapter = chapterDao.get(1).get();
        Assert.assertEquals(1, chapter.getChapter_no());
        // get first chapter in Exodus
        chapter = chapterDao.get(51).get();
        Assert.assertEquals(1, chapter.getChapter_no());
        Assert.assertEquals(2, chapter.getBookId());
    }

    private long insertChpaters() {
        TreeMap<Integer, Integer> args = new TreeMap<>();
        args.put(1, 50);
        args.put(2, 40);
        args.put(3, 27);
        args.put(4, 36);
        args.put(5, 34);
        args.put(6, 24);
        args.put(7, 21);
        args.put(8, 4);
        args.put(9, 31);
        args.put(10, 24); // 2 Samuel

        long updatedRows = chapterDao.insertAllChapters(args);
        return updatedRows;
    }
}
