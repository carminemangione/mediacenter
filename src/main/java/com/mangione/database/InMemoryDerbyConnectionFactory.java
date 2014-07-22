package com.mangione.database;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class InMemoryDerbyConnectionFactory extends AbstractDataSource {

    private static final Logger LOG = LoggerFactory.getLogger(InMemoryDerbyConnectionFactory.class);

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String URL = "jdbc:derby:";
    private String databaseName;

    public InMemoryDerbyConnectionFactory(String databaseName) throws ClassNotFoundException, SQLException {
        this.databaseName = databaseName;
        Class.forName(DRIVER);
        DriverManager.getConnection(URL + "memory:" + databaseName + ";create=true");
    }

    @Override
    public Connection getConnection() throws DatabaseException {
        Connection connection;
        try {
            connection = DriverManager.getConnection(URL + "memory:" + databaseName + ";");
        } catch (SQLException e) {
            LOG.error("Exception creating connection", e);
            throw new DatabaseException(e);
        }
        return connection;
    }
}

