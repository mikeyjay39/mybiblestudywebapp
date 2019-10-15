package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.User;
import com.mybiblestudywebapp.persistence.UserDao;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
public class UserUnitTests {

    private UserDao userDao;
    private User user;


    public UserUnitTests() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DbConnectionTest.getEmbeddedPostgres());
        userDao = new UserDao(jdbcTemplate);
        user = createTestUser();
    }

    @Test
    public void testAddUser() throws Exception {
        addUser(user);
    }

    @Test
    public void testGetUser() throws Exception {
        addUser(user);
        User resultUser = getUserByEmail(user.getEmail());
        Assert.assertNotNull(resultUser);
    }

    @Test
    public void testGetAll() throws Exception {
        // insert users
        create3TestUsers();

        List<User> users = userDao.getAll();
        Assert.assertNotNull(users);
    }

    private User getUserByEmail(String email) {
        long id = -1;
        User user = userDao.get(email).orElse(null);
        return user;
    }

    /**
     * Creates a list of 3 users
     * @return
     */
    private List<User> create3TestUsers() throws Exception {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setEmail("test_" + i + "@gmail.com");
            user.setFirstname("Han" + i);
            user.setLastname("Solo" + i);
            user.setPassword("HASH" + (i + i * i - i));
            users.add(user);
            addUser(user);
        }

        return users;
    }

    private User createTestUser() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setFirstname("Han");
        user.setLastname("Solo");
        user.setPassword("12345");
        return user;
    }

    private void addUser(User user) throws Exception {
        boolean result = userDao.save(user);
        Assert.assertTrue(result);
    }
}
