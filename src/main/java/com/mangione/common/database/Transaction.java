package com.mangione.common.database;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class Transaction {

    private static final Logger LOG = LoggerFactory.getLogger(Transaction.class);

    public Transaction(DataSource connectionFactory) throws SQLException {
        Connection connection = null;

        boolean oldAutoCommit;
        try {
            connection = getConnection(connectionFactory);
            oldAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            doTransaction(connection);
            connection.commit();
            connection.setAutoCommit(oldAutoCommit);
            connection.close();
        } catch (Throwable e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Throwable t) {
                LOG.error("Exception trying to rollback transaction", t);
            }
            throw new SQLException(e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    protected Connection getConnection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }


    protected abstract void doTransaction(Connection connection) throws SQLException;
}
