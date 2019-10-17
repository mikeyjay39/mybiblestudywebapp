package com.mybiblestudywebapp.persistence;

import com.sun.source.tree.Tree;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Created by Michael Jeszenka.
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 10/17/19
 */
public class ChapterDao implements Dao<Chapter> {

    private JdbcTemplate jdbcTemplate;

    public ChapterDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Chapter> get(long id) {
        String sql = "SELECT * FROM chapters WHERE chapter_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{id}, ChapterDao::mapRow));
    }

    /**
     * Do not use this
     * @param uniqueKey
     * @return
     */
    @Deprecated
    @Override
    public Optional<Chapter> getUnique(String uniqueKey) {
        String sql = "SELECT * FROM chapters WHERE chapter_no = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new Object[]{uniqueKey}, ChapterDao::mapRow));
    }

    /**
     * Get all chapters
     * @return
     */
    @Override
    public List<Chapter> getAll() {
        String sql = "SELECT * FROM chapters";
        List<Chapter> results = jdbcTemplate.query(sql, ChapterDao::mapRow);
        return results;
    }

    /**
     * Use this method to insert all the chapters needed into the data base.
     *
     * @return
     */
    private long insertAllChapters() {
        TreeMap<Integer, Integer> args = new TreeMap<>();
        args.put(1, 50);
        args.put(2, 40);
        args.put(3, 27);
        args.put(4, 36);
        args.put(5, 34);
        args.put(6, 24);
        args.put(7, 21);
        args.put(8, 4);
        args.put(9, 31);
        args.put(10, 24); // 2 Samuel
        args.put(11, 22);
        args.put(12, 25);
        args.put(13, 29);
        args.put(14, 36);
        args.put(15, 10);
        args.put(16, 13);
        args.put(17, 10);
        args.put(18, 42);
        args.put(19, 150);
        args.put(20, 31); // Proverbs
        args.put(21, 12);
        args.put(22, 8);
        args.put(23, 66);
        args.put(24, 52);
        args.put(25, 5);
        args.put(26, 48);
        args.put(27, 12);
        args.put(28, 14);
        args.put(29, 3);
        args.put(30, 9);
        args.put(31, 1);
        args.put(32, 4);
        args.put(33, 7);
        args.put(34, 3);
        args.put(35, 3);
        args.put(36, 3);
        args.put(37, 2);
        args.put(38, 14);
        args.put(39, 4);
        args.put(40, 28); // Matthew
        args.put(41, 16);
        args.put(42, 24);
        args.put(43, 21);
        args.put(44, 28);
        args.put(45, 16);
        args.put(46, 16);
        args.put(47, 13);
        args.put(48, 6);
        args.put(49, 6);
        args.put(50, 4); // Philippians
        args.put(51, 4);
        args.put(52, 5);
        args.put(53, 3);
        args.put(54, 6);
        args.put(55, 4);
        args.put(56, 3);
        args.put(57, 1);
        args.put(58, 13);
        args.put(59, 5);
        args.put(60, 5);
        args.put(61, 3);
        args.put(62, 5);
        args.put(63, 1);
        args.put(64, 1);
        args.put(65, 1);
        args.put(66, 22);


        long totalRows = 0;
        for (int i = 0; i < args.size(); i++) {
            int book_id = i + 1;
            int chapters = args.get(book_id);
            for (int j = 0; j < chapters; j ++) {
                int chapter_no = j + 1;
                String sql = "INSERT INTO chapters (book_id, chapter_no) " +
                        "VALUES (?, ?)";
                jdbcTemplate.update(sql, book_id, chapter_no);
                totalRows++;
            }
        }
        return totalRows;
    }

    private static Chapter mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chapter chapter = new Chapter();
        chapter.setChapterId(rs.getInt("chapter_id"));
        chapter.setBookId(rs.getInt("book_id"));
        chapter.setChapter_no(rs.getInt("chapter_no"));
        return chapter;
    }
}
