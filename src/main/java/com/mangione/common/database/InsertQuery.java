package com.mangione.common.database;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class InsertQuery extends Query {

    private static final Logger LOG = LoggerFactory.getLogger(InsertQuery.class);

    private Long[] generatedKeys = new Long[0];

    public InsertQuery(Connection connection) throws SQLException {
        super(connection);
        super.execute(connection);
    }

    public Long[] getGeneratedKeys() {
        return generatedKeys;
    }

    protected String getQueryString() {
        return getInsertQuery();
    }

    protected void runQueryAndProcessResults(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.executeUpdate();

        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs != null) {
            List<Long> keys = new ArrayList<Long>();
            while (rs.next()) {
                keys.add(rs.getLong(1));
            }
            generatedKeys = keys.toArray(new Long[keys.size()]);
        } else {
            generatedKeys = new Long[0];
        }
        super.closePreparedStatementAndResultSet();
    }


    protected abstract String getInsertQuery();

    protected void processResults(ResultSet rs) throws SQLException {
    }

    @Override
    protected PreparedStatement getPreparedStatement() throws SQLException {
        String queryString = getQueryString();
        LOG.debug(queryString);
        return getConnection().prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    protected void execute(Connection connection) throws SQLException {
    }

    protected void closePreparedStatementAndResultSet() throws SQLException {
    }

}
