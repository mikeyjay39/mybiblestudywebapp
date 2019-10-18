package com.mybiblestudywebapp.unittests;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
    private static String sqlDir = "/home/michael/Projects/mybiblestudywebapp/sql/";

    /*@BeforeClass
    public static void beforeClass() throws Exception {*/
    static {
        try {
            embeddedDataSource = buildEmbeddedDataBase();
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
     * @throws Exception
     */
    @Test
    public void testEmbeddedPg() throws Exception
    {
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
     * @return
     */
    public static DataSource getEmbeddedPostgres() {
        return embeddedDataSource;
    }

    /**
     * Builds embedded postgres db for testing
     * @return
     * @throws Exception
     */
    private static DataSource buildEmbeddedDataBase() throws Exception {
        EmbeddedPostgres pg = EmbeddedPostgres.start();
        DataSource dataSource = pg.getPostgresDatabase();

        try (
                Connection connection = dataSource.getConnection()
        ) {

            ScriptUtils.executeSqlScript(connection,
                    new EncodedResource(
                            new FileSystemResource(
                                    Paths.get(
                                            sqlDir + "schema.sql"))));
        }

        try (
                Connection connection = dataSource.getConnection()
                ) {

            ScriptUtils.executeSqlScript(connection,
                    new EncodedResource(
                            new FileSystemResource(
                                    Paths.get(
                                            sqlDir + "backups/mybiblestudydb.sql"))));
        }
        return dataSource;
    }

    public static DataSource getLiveDataSource() {
        return liveDataSource;
    }
}


