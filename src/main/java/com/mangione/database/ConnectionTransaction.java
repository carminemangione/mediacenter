package com.mangione.database;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ConnectionTransaction {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionTransaction.class);

    public ConnectionTransaction(Connection connection) throws SQLException {

        boolean oldAutoCommit;
        try {
            oldAutoCommit = connection.getAutoCommit();
            if (oldAutoCommit) {
                connection.setAutoCommit(false);
            }
            doTransaction(connection);
            connection.commit();
            if (oldAutoCommit) {
                connection.setAutoCommit(oldAutoCommit);
            }

        } catch (Throwable e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Throwable t) {
                LOG.error("Exception trying to rollback transaction", t);
            }
            throw new SQLException(e);
        }
    }

    protected abstract void doTransaction(Connection connection) throws SQLException;
}
