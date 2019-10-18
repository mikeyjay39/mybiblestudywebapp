package com.mybiblestudywebapp.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/18/19
 */
public class ViewDao implements UpdatableDao<View> {

    private static final Logger logger = LoggerFactory.getLogger(ViewDao.class);
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ViewDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }


    /**
     * Creates a View in the DB
     * @param view
     * @return
     */
    @Override
    public boolean save(View view) {
        String sql = "INSERT INTO views (user_id, priv) " +
                "VALUES (:userId, :priv)";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", view.getUserId())
                .addValue("priv", view.isPriv());
        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, params, holder);
        return rows > 0;
    }

    /**
     * Update existing View in DB
     * @param view
     * @return true on success
     */
    @Override
    public boolean update(View view) {
        String sql = "UPDATE views SET priv = :priv WHERE view_id = :viewId";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("viewId", view.getViewId())
                .addValue("priv", view.isPriv());
        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, params, holder);
        return rows > 0;
    }

    /**
     * Delete a View from the DB based on view_id
     * @param view
     * @return true on success
     */
    @Override
    public boolean delete(View view) {
        String sql = "DELETE FROM views WHERE view_id = :viewId";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("viewId", view.getViewId());
        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, parameterSource, holder);
        return rows > 0;
    }

    /**
     * Query DB for a single view
     * @param id
     * @return
     */
    @Override
    public Optional<View> get(long id) {
        String sql = "SELECT * FROM views WHERE view_id = :viewId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("viewId", id);
        View result = null;
        try {
            result = namedParameterJdbcTemplate.queryForObject(sql, params, ViewDao::mapRow);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Could not get view_id = " + id + "\n" + e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<View> getUnique(String uniqueKey) {
        String sql = "SELECT * FROM views WHERE view_code = :viewCode";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("viewCode", uniqueKey, Types.OTHER);
        View result = null;
        try {
            result = namedParameterJdbcTemplate.queryForObject(sql, params, ViewDao::mapRow);
        } catch (EmptyResultDataAccessException e) {
            logger.info("Could not get view_code = " + uniqueKey + "\n" + e.getMessage());
        }
        return Optional.ofNullable(result);    }

    @Override
    public List<View> getAll() {
        String sql = "SELECT * FROM views";
        return jdbcTemplate.query(sql, ViewDao::mapRow);
    }

    private static View mapRow(ResultSet rs, int rowNum) throws SQLException {
        View view = new View();
        view.setViewId(rs.getInt("view_id"));
        view.setUserId(rs.getInt("user_id"));
        view.setViewCode(rs.getString("view_code"));
        view.setPriv(rs.getBoolean("priv"));
        return view;
    }
}
