package com.mangione.common.database;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public class DerbyConnectionFactory extends AbstractDataSource {

    private static final Logger LOG = LoggerFactory.getLogger(DerbyConnectionFactory.class);

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String URL = "jdbc:derby:";
    private final String databaseDirectory;
    private String databaseName;

    public DerbyConnectionFactory(String databaseDirectory, String databaseName) throws ClassNotFoundException, SQLException {
        this.databaseName = databaseName;
        this.databaseDirectory = databaseDirectory;
        Class.forName(DRIVER);
        DriverManager.getConnection(URL + databaseDirectory + "/" + databaseName + ";create=true");
    }

    @Override
    public Connection getConnection() throws DatabaseException {
        Connection connection;
        try {
            connection = DriverManager.getConnection(URL + databaseDirectory + "/" + databaseName + ";");
        } catch (SQLException e) {
            LOG.error("Exception creating connection", e);
            throw new DatabaseException(e);
        }
        return connection;
    }


    public boolean doesTableExist(String schemaName, String tableName) throws SQLException {
        boolean tableExists;
        try (Connection connection = getConnection()) {
            final DatabaseMetaData metaData = connection.getMetaData();
            final ResultSet rs = metaData.getTables(null, null, tableName.toUpperCase(), null);
            tableExists = rs.next();
        }
        return tableExists;
    }

}

