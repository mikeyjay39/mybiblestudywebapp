package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.View;
import com.mybiblestudywebapp.persistence.ViewDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/18/19
 */
public class ViewUnitTests {

    private View view;
    private ViewDao viewDao;

    public ViewUnitTests() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DbConnectionTest.getEmbeddedPostgres());
        viewDao = new ViewDao(jdbcTemplate);
    }

    @Before
    public void setUp() {
        view = null;
    }

    @After
    public void tearDown() {
        view = null;
    }

    @Test
    public void testAddAndGet() {
        addViewAndGet();
    }

    @Test
    public void testUpdate() {
        view = addViewAndGet();
        view.setPriv(true);
        viewDao.update(view);
        View newView = viewDao.get(1).get();
        Assert.assertTrue(newView.isPriv());
    }

    @Test
    public void testDelete() {
        view = addViewAndGet();
        boolean result = viewDao.delete(view);
        Assert.assertTrue(result);
        var returnedView = viewDao.get(1);
        Assert.assertTrue(returnedView.isEmpty());
    }

    @Test
    public void testGetUnique() {
        view = addViewAndGet();
        String viewCode = view.getViewCode();
        var result = viewDao.getUnique(viewCode);
        View newView = result.get();
        Assert.assertNotNull(newView);
    }

    private View addViewAndGet() {
        view = new View();
        view.setUserId(1);
        view.setPriv(false);
        boolean result = viewDao.save(view);
        Assert.assertTrue(result);
        Optional<View> optionalView = viewDao.get(1);
        View newView = optionalView.get();
        Assert.assertNotNull(newView.getViewCode());
        return newView;
    }
}
