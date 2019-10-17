package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.Chapter;
import com.mybiblestudywebapp.persistence.ChapterDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
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
        //long rows = chapterDao.insertAllChapters();

        // get first chapter in Genesis
        chapter = chapterDao.get(1).get();
        Assert.assertEquals(1, chapter.getChapter_no());
        // get first chapter in Exodus
        chapter = chapterDao.get(51).get();
        Assert.assertEquals(1, chapter.getChapter_no());
        Assert.assertEquals(2, chapter.getBookId());
    }

    /**
     * Use this once on the live DB to build rows for chapters table
     * @throws Exception
     */
    /*@Test
    public void insertAllChapters() throws Exception {

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(System.getenv("PSQLDBDRIVER"));
        dataSourceBuilder.url(System.getenv("PSQLDBURL"));
        dataSourceBuilder.username(System.getenv("PSQLDBUSER"));
        dataSourceBuilder.password(System.getenv("PSQLDBPASS"));
        DataSource dataSource = dataSourceBuilder.build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        chapterDao = new ChapterDao(jdbcTemplate);


        long rows = chapterDao.insertAllChapters();

        Assert.assertEquals(1189, rows);

        // get first chapter in Genesis
        chapter = chapterDao.get(1).get();
        Assert.assertEquals(1, chapter.getChapter_no());
        // get first chapter in Exodus
        chapter = chapterDao.get(51).get();
        Assert.assertEquals(1, chapter.getChapter_no());
        Assert.assertEquals(2, chapter.getBookId());
    }*/


}
