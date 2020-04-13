package com.mybiblestudywebapp.persistenceservice.persistence;

import com.mybiblestudywebapp.utils.persistence.model.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mybiblestudywebapp.utils.main.Constants.VIEW_CODE;
import static com.mybiblestudywebapp.utils.main.Constants.VIEW_ID;


/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/18/19
 */
@Component
public class ViewDao implements UpdatableDao<View> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewDao.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ViewDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Creates a View in the DB
     *
     * @param view
     * @return view_id or -1 on failure
     */
    @Override
    public long save(View view) {
        String sql = "INSERT INTO views (user_id, priv) " +
                "VALUES (:userId, :priv)" +
                "RETURNING view_id";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", view.getUserId())
                .addValue("priv", view.isPriv());
        long viewId = -1;

        try {
            viewId = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        } catch (DataAccessException e) {
            String errMsg = "Could not add view for user_id: " + view.getUserId() +
                    "\n" + e.getMessage();
            LOGGER.info(errMsg);
        }

        return viewId;
    }

    /**
     * Update existing View in DB
     *
     * @param view
     * @return true on success
     */
    @Override
    public boolean update(View view) {
        String sql = "UPDATE views SET priv = :priv WHERE view_id = :viewId";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(VIEW_ID, view.getViewId())
                .addValue("priv", view.isPriv());
        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, params, holder);
        return rows > 0;
    }

    /**
     * Delete a View from the DB based on view_id
     *
     * @param view
     * @return true on success
     */
    @Override
    public boolean delete(View view) {
        String sql = "";
        SqlParameterSource parameterSource;

        if (view.getViewCode() == null) {
            sql = "DELETE FROM views WHERE view_id = :viewId";
            parameterSource = new MapSqlParameterSource()
                    .addValue(VIEW_ID, view.getViewId());
        } else {
            sql = "DELETE FROM views WHERE view_code = :viewCode";
            parameterSource = new MapSqlParameterSource()
                    .addValue(VIEW_CODE, view.getViewCode(), Types.OTHER);
        }

        KeyHolder holder = new GeneratedKeyHolder();
        int rows = 0;
        rows = namedParameterJdbcTemplate.update(sql, parameterSource, holder);
        return rows > 0;
    }

    /**
     * Query DB for a single view
     *
     * @param id
     * @return
     */
    @Override
    public Optional<View> get(long id) {
        String sql = "SELECT * FROM views WHERE view_id = :viewId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(VIEW_ID, id);
        View result = null;
        try {
            result = namedParameterJdbcTemplate.queryForObject(sql, params, ViewDao::mapRow);
        } catch (EmptyResultDataAccessException e) {
            String errMsg = "Could not get view_id = " + id + "\n" + e.getMessage();
            LOGGER.error(errMsg);
        }
        return Optional.ofNullable(result);
    }

    /**
     * @param args key = viewCode
     * @return
     */
    @Override
    public Optional<List<View>> get(Map<String, Object> args) {
        // 0 is used to denote a generic view to contain all public notes
        if ("0".equals(args.get(VIEW_CODE))) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM views WHERE view_code = :viewCode";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue(VIEW_CODE, args.get(VIEW_CODE), Types.OTHER);
        List<View> result = null;
        try {
            result = namedParameterJdbcTemplate.query(sql, params, ViewDao::mapRow);
        } catch (EmptyResultDataAccessException e) {
            String errMsg = "Could not get view_code = " + args.get(VIEW_CODE) + "\n" + e.getMessage();
            LOGGER.info(errMsg);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public List<View> getAll() {
        String sql = "SELECT * FROM views";
        return jdbcTemplate.query(sql, ViewDao::mapRow);
    }

    /**
     * Get all the view codes for a user
     *
     * @param userId
     * @return
     */
    public List<String> getAllCodesForUser(long userId) {
        String sql = "Select view_code FROM views " +
                "WHERE user_id = :userId";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId);

        return namedParameterJdbcTemplate.queryForList(sql, params, String.class);
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
