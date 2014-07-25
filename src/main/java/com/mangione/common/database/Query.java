package com.mangione.common.database;


import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.List;

import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.readLines;

public abstract class Query {

    private static final Logger LOG = LoggerFactory.getLogger(Query.class);
    private int totalNumberOfQueries;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet rs;
    private int queryNumber;
    private boolean inTransaction;

    public Query(DataSource dataSource) throws SQLException {
        execute(dataSource.getConnection());
    }


    protected Query(Connection connection) throws SQLException {
        inTransaction = true;
        execute(connection);
    }


    protected abstract String getQueryString() throws SQLException;

    protected abstract void bindQueryParameters(PreparedStatement ps) throws SQLException;

    protected abstract void processResults(ResultSet rs) throws SQLException;

    protected void execute(Connection connection) throws SQLException {
        queryNumber = totalNumberOfQueries++;
        try {
            this.connection = connection;
            long start = System.currentTimeMillis();
            preparedStatement = getPreparedStatement();
            bindQueryParameters(preparedStatement);
            runQueryAndProcessResults(preparedStatement);
            if (LOG.isDebugEnabled()) {
                long end = System.currentTimeMillis();
                long executionTime = end - start;
                LOG.debug("Q" + queryNumber + ": Executed  and processed in " + executionTime + " for scoring: " + getQueryString());
            }
        } finally {
            closePreparedStatementAndResultSet();
            closeConnectionIfNeeded();
        }
    }

    protected void runQueryAndProcessResults(PreparedStatement preparedStatement) throws SQLException {
        rs = preparedStatement.executeQuery();

        long start = System.currentTimeMillis();


        processResults(rs);

        if (LOG.isDebugEnabled()) {
            long end = System.currentTimeMillis();
            long executionTime = end - start;
            LOG.debug("Q" + queryNumber + ": Processed scoring results in " + executionTime + " milliseconds");
        }
    }

    protected void closePreparedStatementAndResultSet() throws SQLException {
        if (rs != null && !rs.isClosed()) {
            rs.close();
        }
        if (preparedStatement != null && !preparedStatement.isClosed()) {
            preparedStatement.close();
        }
    }

    protected void closeConnectionIfNeeded() throws SQLException {
        if (connection != null && !inTransaction && !connection.isClosed()) {
            connection.close();
        }
    }

    protected PreparedStatement getPreparedStatement() throws SQLException {
        String queryString = getQueryString();
        LOG.debug(queryString);
        return connection.prepareStatement(queryString);
    }

    protected Connection getConnection() {
        return connection;
    }

    protected Array createLongArray(Collection<Long> longs) throws SQLException {
        return connection.createArrayOf("bigint", longs.toArray(new Long[longs.size()]));
    }

    protected Array createStringArray(Collection<String> strings) throws SQLException {
        return connection.createArrayOf("varchar", strings.toArray(new String[strings.size()]));
    }

    public static String readQueryFromResource(String resourcePath) {
        try {
            List<String> lines = readLines(getResource(resourcePath), Charsets.UTF_8);
            return Joiner.on("\n").join(lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
