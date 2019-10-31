package com.mybiblestudywebapp.unittests;

import com.mybiblestudywebapp.persistence.model.User;
import com.mybiblestudywebapp.persistence.UserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.*;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
public class UserUnitTests {

    private UserDao userDao;
    private User user;
    private Map<String, Object> args = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DbConnectionTest.rebuildEmbeddedDataBase());
        userDao = new UserDao(jdbcTemplate,
                new NamedParameterJdbcTemplate(jdbcTemplate));
    }


    public UserUnitTests() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(DbConnectionTest.getEmbeddedPostgres());
        userDao = new UserDao(jdbcTemplate, new NamedParameterJdbcTemplate(jdbcTemplate));
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

    @Test
    public void testUpdate() throws Exception {
        User user = createTestUser();
        addUser(user);
        user = getUserByEmail(user.getEmail());
        long originalId = user.getUserId();
        String newEmail = "mynewemail@gmail.com";
        args.put("email", newEmail);
        userDao.update(user, args);
        User newUser = getUserByEmail(newEmail);
        long newId = newUser.getUserId();
        Assert.assertEquals(originalId, newId);
        Assert.assertEquals(user, newUser);
        Assert.assertNotEquals(user.getEmail(), newUser.getEmail());
    }

    @Test
    public void testDelete() throws Exception {
        User user = createTestUser();
        addUser(user);
        user = getUserByEmail(user.getEmail());
        long originalId = user.getUserId();
        boolean result = userDao.delete(user);
        Optional<User> deletedUser = userDao.get(originalId);
        Assert.assertTrue(deletedUser.isEmpty());
        Assert.assertTrue(result);
    }

    @Test
    public void testUpdate2() {
        //userDao = new UserDao(new JdbcTemplate(DbConnectionTest.getLiveDataSource()));
        User user = createTestUser();
        addUser(user);
        User retrievedUser = getUserByEmail(user.getEmail());
        Assert.assertEquals("Solo", user.getLastname());
        retrievedUser.setLastname("Not Solo");
        userDao.update(retrievedUser);
        User updatedUser = userDao.get(retrievedUser.getUserId()).get();
        Assert.assertEquals("Not Solo", updatedUser.getLastname());

    }

    private User getUserByEmail(String email) {
        long id = -1;
        args.put("email", email);
        User user = userDao.get(args).orElse(null).get(0);
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

    private void addUser(User user) {
        long result = userDao.save(user);
        Assert.assertTrue(result > -1);
    }
}
