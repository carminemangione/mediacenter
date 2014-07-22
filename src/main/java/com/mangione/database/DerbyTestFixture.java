package com.mangione.database;

import org.junit.After;
import org.junit.Before;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DerbyTestFixture {

    private final DataSource dataSource;

    public DerbyTestFixture() {
        try {
            dataSource = new InMemoryDerbyConnectionFactory("tests");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setupRunner() throws Exception {
        setup();
    }

    @After
    public void teardownRunners() throws Exception {
        teardown();
        File derbyLog = new File("derby.log");
        if (derbyLog.exists()) {
            //noinspection ResultOfMethodCallIgnored
            derbyLog.delete();
        }
    }


    public abstract void setup() throws Exception;

    public abstract void teardown() throws Exception;

    protected DataSource getDataSource() {
        return dataSource;
    }



    public static void dropTableIfExists(Connection connection, String tableName) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE " + tableName)){
            preparedStatement.execute();
        } catch (SQLException e) {
            boolean canIgnore = "42Y55".equalsIgnoreCase(e.getSQLState()) || "42Y07".equalsIgnoreCase(e.getSQLState());
            if (!canIgnore)
                throw e;
        }
    }

    public static void dropTableIfExists(DataSource factory, String tableName) throws SQLException {

          try (Connection connection = factory.getConnection()) {
             dropTableIfExists(connection, tableName);
          }
      }

    protected static void executeUpdate(Connection connection, String updateString) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateString)) {
            preparedStatement.executeUpdate();
        }
    }

    protected static void executeUpdate(DataSource connectionFactory, String updateString) throws SQLException {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateString)) {
            preparedStatement.executeUpdate();
        }
    }

    protected void executeUpdate(String updateString) throws SQLException {
        executeUpdate(getDataSource(), updateString);
    }

    protected static int executeCount(Connection connection, String countQueryString) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(countQueryString);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            return count;
        }
    }

    protected static int executeCount(DataSource connectionFactory,
            String countQueryString) throws SQLException {
        try (Connection connection = connectionFactory.getConnection()) {
            return executeCount(connection, countQueryString);
        }
    }
}

