package com.mybiblestudywebapp.persistenceservice.unittests;

import com.mybiblestudywebapp.utils.persistence.model.Chapter;
import com.mybiblestudywebapp.persistenceservice.persistence.ChapterDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/17/19
 */
@Transactional
@Rollback
public class ChapterUnitTests {

    private Chapter chapter;
    private ChapterDao chapterDao;

    public ChapterUnitTests() {
        JdbcTemplate jdbcTemplate = DbConnectionTest.getJdbcTemplate();
        chapterDao = new ChapterDao(jdbcTemplate, new NamedParameterJdbcTemplate(jdbcTemplate));
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
        Assert.assertEquals(1, chapter.getChapterNo());
        // get first chapter in Exodus
        chapter = chapterDao.get(51).get();
        Assert.assertEquals(1, chapter.getChapterNo());
        Assert.assertEquals(2, chapter.getBookId());
    }

    @Test
    public void testGetAll() throws Exception {
        var result = chapterDao.getAll();
        Assert.assertEquals(1189, result.size());
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
