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
        long rows = insertChapters();

        // get first chapter in Genesis
        chapter = chapterDao.get(1).get();
        Assert.assertEquals(1, chapter.getChapter_no());
        // get first chapter in Exodus
        chapter = chapterDao.get(51).get();
        Assert.assertEquals(1, chapter.getChapter_no());
        Assert.assertEquals(2, chapter.getBookId());
    }

    private long insertChapters() {
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
        args.put(11, 22);
        args.put(12, 25);
        args.put(13, 29);
        args.put(14, 36);
        args.put(15, 10);
        args.put(16, 13);
        args.put(17, 10);
        args.put(18, 42);
        args.put(19, 150);
        args.put(20, 31); // Proverbs
        args.put(21, 12);
        args.put(22, 8);
        args.put(23, 66);
        args.put(24, 52);
        args.put(25, 5);
        args.put(26, 48);
        args.put(27, 12);
        args.put(28, 14);
        args.put(29, 3);
        args.put(30, 9);
        args.put(31, 1);
        args.put(32, 4);
        args.put(33, 7);
        args.put(34, 3);
        args.put(35, 3);
        args.put(36, 3);
        args.put(37, 2);
        args.put(38, 14);
        args.put(39, 4);
        args.put(40, 28); // Matthew
        args.put(41, 16);
        args.put(42, 24);
        args.put(43, 21);
        args.put(44, 28);
        args.put(45, 16);
        args.put(46, 16);
        args.put(47, 13);
        args.put(48, 6);
        args.put(49, 6);
        args.put(50, 4); // Philippians
        args.put(51, 4);
        args.put(52, 5);
        args.put(53, 3);
        args.put(54, 6);
        args.put(55, 4);
        args.put(56, 3);
        args.put(57, 1);
        args.put(58, 13);
        args.put(59, 5);
        args.put(60, 5);
        args.put(61, 3);
        args.put(62, 5);
        args.put(63, 1);
        args.put(64, 1);
        args.put(65, 1);
        args.put(66, 22);

        long updatedRows = chapterDao.insertAllChapters(args);
        return updatedRows;
    }
}
