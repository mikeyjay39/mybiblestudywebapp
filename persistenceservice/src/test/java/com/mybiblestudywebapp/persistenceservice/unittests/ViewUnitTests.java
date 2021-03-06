package com.mybiblestudywebapp.persistenceservice.unittests;

import com.mybiblestudywebapp.utils.persistence.model.View;
import com.mybiblestudywebapp.persistenceservice.persistence.ViewDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/18/19
 */
public class ViewUnitTests {

    private View view;
    private ViewDao viewDao;
    private Map<String, Object> args = new HashMap<>();


    public ViewUnitTests() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DbConnectionTest.getEmbeddedPostgres());
        viewDao = new ViewDao(jdbcTemplate, new NamedParameterJdbcTemplate(jdbcTemplate));
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
        View newView = viewDao.get(view.getViewId()).get();
        Assert.assertTrue(newView.isPriv());
    }

    @Test
    public void testDelete() {
        view = addViewAndGet();
        boolean result = viewDao.delete(view);
        Assert.assertTrue(result);
        var returnedView = viewDao.get(view.getViewId());
        Assert.assertTrue(returnedView.isEmpty());
    }

    @Test
    public void testGetUnique() {
        view = addViewAndGet();
        String viewCode = view.getViewCode();
        args.put("viewCode", viewCode);
        var result = viewDao.get(args);
        View newView = result.get().get(0);
        Assert.assertNotNull(newView);
    }

    @Test
    public void testGetAll() {
        add3Views();
        var result = viewDao.getAll();
        Assert.assertTrue(result.size() > 1);
    }

    private View addViewAndGet() {
        view = new View();
        view.setUserId(1);
        view.setPriv(false);
        long result = viewDao.save(view);
        Assert.assertTrue(result > -1);
        Optional<View> optionalView = viewDao.get(result);
        View newView = optionalView.get();
        Assert.assertNotNull(newView.getViewCode());
        return newView;
    }

    private void add3Views() {
        for (int i = 0; i < 3; i++) {
            view = new View();
            view.setUserId(1);
            view.setPriv(false);
            long result = viewDao.save(view);
            Assert.assertTrue(result > -1);
        }
    }
}
