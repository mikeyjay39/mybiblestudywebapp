package com.mybiblestudywebapp.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 14.
 */
@Component
public class UserDao implements UpdatableDao<User> {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDao(){}

    public UserDao(JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }



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
     * @return user_id or -1 on failure
     */
    @Override
    public long save(User user) {
        String sql = "INSERT INTO users (email, firstname, lastname, password) " +
                "VALUES (:email, :firstname, :lastname, :password) " +
                "RETURNING user_id";

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("firstname", user.getFirstname())
                .addValue("lastname", user.getLastname())
                .addValue("password", user.getPassword());

        long userId = -1;
        try {
            userId = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        } catch (DataAccessException e) {
            String errMsg = "Could not add user for email: " + user.getEmail() +
                    "\n" + e.getMessage();
            logger.info(errMsg);
        }

        return userId;
    }

    /**
     * Updates the entry in the users table
      * @param user
     * @return
     */
    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET email = :email, " +
                "firstname = :firstname, lastname = :lastname, password = :password, " +
                "ranking = :ranking, created_at = :createdAt WHERE user_id = :userId";

        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("userId", user.getUserId())
                .addValue("firstname", user.getFirstname())
                .addValue("lastname", user.getLastname())
                .addValue("password", user.getPassword())
                .addValue("ranking", user.getRanking())
                .addValue("createdAt", Timestamp.valueOf(user.getCreatedAt()));

        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, params, holder);
        return rows > 0;
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
