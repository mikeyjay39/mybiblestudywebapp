package com.mybiblestudywebapp.persistenceservice.unittests;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Created by Michael Jeszenka
 * <a href="mailto:michael@jeszenka.com">michael@jeszenka.com</a>
 * 2019. 10. 11.
 */
public class DbConnectionTest {

    private static DataSource embeddedDataSource;
    private static Connection livePostgresConnection;
    private static DataSource liveDataSource;
    private static String sqlDir = "/home/michael/Projects/mybiblestudywebapp/mybiblestudyservice/sql/";
    private static JdbcTemplate jdbcTemplate;

    /*@BeforeClass
    public static void beforeClass() throws Exception {*/
    static {
        try {
            // build embedded DB
            embeddedDataSource = buildEmbeddedDataBase();
            jdbcTemplate = new JdbcTemplate(embeddedDataSource);

            Properties properties = new Properties();
            properties.setProperty("user", System.getenv("PSQLDBUSER"));
            properties.setProperty("password", System.getenv("PSQLDBPASS"));
            livePostgresConnection = DriverManager.getConnection(System.getenv("PSQLDBURL"), properties);
        } catch (Exception e) {
            System.err.println("Failed to build embedded db for testing" + e.getMessage());
        }

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(System.getenv("PSQLDBDRIVER"));
        dataSourceBuilder.url(System.getenv("PSQLDBURL"));
        dataSourceBuilder.username(System.getenv("PSQLDBUSER"));
        dataSourceBuilder.password(System.getenv("PSQLDBPASS"));
        liveDataSource = dataSourceBuilder.build();
    }

    /**
     * Tests that connection to live postgres works
     *
     * @throws Exception
     */
    @Test
    public void testLiveDbConnection() throws Exception {
        Statement statement = livePostgresConnection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT 1");
        Assert.assertTrue(rs.next());
        Assert.assertEquals(1, rs.getInt(1));
        Assert.assertFalse(rs.next());
    }


    /**
     * Tests that the connection to embedded postgres works
     *
     * @throws Exception
     */
    @Test
    public void testEmbeddedPg() throws Exception {
        try (EmbeddedPostgres pg = EmbeddedPostgres.start();
             Connection connection = pg.getPostgresDatabase().getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT 1");
            Assert.assertTrue(rs.next());
            Assert.assertEquals(1, rs.getInt(1));
            Assert.assertFalse(rs.next());
        } catch (Exception e) {
            System.out.print(e);
            Assert.fail();
        }
    }

    /**
     * See if we were able to import the schema into the embedded DB
     */
    @Test
    public void testEmbeddedPgBuilt() throws Exception {
        try (Connection connection = embeddedDataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM books;");
            Assert.assertTrue(rs.next());
            Assert.assertEquals("Genesis", rs.getString("title"));
        } catch (Exception e) {
            System.out.print(e);
            Assert.fail();
        }
    }

    /**
     * Provides the embedded Postgres for tests
     *
     * @return
     */
    public static DataSource getEmbeddedPostgres() {
        return embeddedDataSource;
    }

    /**
     * Builds embedded postgres db for testing
     *
     * @return
     * @throws Exception
     */
    private static DataSource buildEmbeddedDataBase() throws Exception {
        EmbeddedPostgres pg = EmbeddedPostgres.start();
        DataSource dataSource = pg.getPostgresDatabase();
        List<String> sqlFiles = new ArrayList<>();
        sqlFiles.add(sqlDir + "extensions.sql");
        sqlFiles.add(sqlDir + "schema.sql");
        sqlFiles.add(sqlDir + "backups/mybiblestudydb.sql");

        for (int i = 0; i < sqlFiles.size(); i++) {
            String sqlFile = sqlFiles.get(i);
            try (
                    Connection connection = dataSource.getConnection()
            ) {

                ScriptUtils.executeSqlScript(connection,
                        new EncodedResource(
                                new FileSystemResource(
                                        Paths.get(
                                                sqlFile))));
            }
        }


        return dataSource;
    }

    public static DataSource rebuildEmbeddedDataBase() throws Exception {
        embeddedDataSource = buildEmbeddedDataBase();
        return embeddedDataSource;
    }

    public static DataSource getLiveDataSource() {
        return liveDataSource;
    }

    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}




