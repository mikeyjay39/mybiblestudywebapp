package com.mybiblestudywebapp.persistence;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
public class UserDao implements UpdatableDao<User> {

    private JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // TODO replace this with JDBC methods
    //private List<User> users = new ArrayList<>();

    @Override
    public Optional<User> get(long id) {

        String sql = "SELECT * FROM users WHERE user_id = ?";
        var result = jdbcTemplate.queryForList(sql, id);
        User user = null;
        if (result.size() > 0) {
            user = buildUser(result.get(0));
        }
        return Optional.ofNullable(user);
    }

    /**
     * Gets user from database based on email value
     * @param email
     * @return User from database
     */
    @Override
    public Optional<User> getUnique(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        var result = jdbcTemplate.queryForList(sql, email);
        User user = null;
        if (result.size() > 0) {
            user = buildUser(result.get(0));
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        var result = jdbcTemplate.queryForList(sql);
        if (result.size() < 1) {
            return null;
        }
        List<User> users = new ArrayList<>();
        for (Map<String, Object> row : result) {
            users.add(buildUser(row));
        }
        return users;
    }

    /**
     * Writes User into database
     * @param user
     * @return true on success
     */
    @Override
    public boolean save(User user) {
        boolean result = false;
        String sql = "INSERT INTO users (email, firstname, lastname, password) VALUES (?, ?, ?, ?);";
        int rows = jdbcTemplate.update(
                sql, user.getEmail(), user.getFirstname(), user.getLastname(), user.getPassword()
        );
        if (rows > 0) {
            result = true;
        }
        return result;
    }

    /**
     * TODO finish this
     * @param user
     * @return
     */
    @Override
    public boolean update(User user) {
        return false;
    }

    /**
     *
     * @param user
     * @param params
     * @return
     */
    @Deprecated
    public boolean update(User user, Map<String, Object> params) {
        Set<String> columns = params.keySet();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE users SET ");
        int columnsSize = columns.size();
        int i = 0;
        for (String column : columns) {
            stringBuilder.append(column + " = '" + params.get(column) + "'");
            i++;
            if (i < columnsSize) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(" WHERE user_id = " + user.getUserId());
        String sql =  stringBuilder.toString();

        int rows = jdbcTemplate.update(sql);

        if (rows < 1) {
            return false;
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        int rows = jdbcTemplate.update(sql, user.getUserId());
        if (rows < 1) {
            return false;
        }
        return true;
    }

    /**
     * Builds a User object after SQL query
     * @param sqlrow The result row
     * @return User DTO
     */
    private User buildUser(Map<String, Object> sqlrow) {
        User user = new User();
        user.setUserId((long)sqlrow.get("user_id"));
        user.setEmail((String)sqlrow.get("email"));
        user.setFirstname((String)sqlrow.get("firstname"));
        user.setLastname((String)sqlrow.get("lastname"));
        user.setPassword((String)sqlrow.get("password"));
        user.setRanking((int)sqlrow.get("ranking"));
        user.setCreatedAt(((Timestamp)sqlrow.get("created_at")).toLocalDateTime());
        return user;
    }
}
