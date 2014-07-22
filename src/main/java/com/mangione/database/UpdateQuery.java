package com.mangione.database;



import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class UpdateQuery extends Query {
    int updateCount;

    public UpdateQuery(Connection connection) throws SQLException {
        super(connection);
    }

    public UpdateQuery(DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    public int getUpdateCount() {
        return updateCount;
    }

    protected String getQueryString() {
        return getUpdateQuery();
    }

    protected void runQueryAndProcessResults(PreparedStatement preparedStatement) throws SQLException {
        updateCount = preparedStatement.executeUpdate();
    }

    protected void processResults(ResultSet rs) throws SQLException {
    }

    protected void closePreparedStatementAndResultSet() throws SQLException {
    }

    protected abstract String getUpdateQuery();

}
