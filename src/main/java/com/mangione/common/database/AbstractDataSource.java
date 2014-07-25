package com.mangione.common.database;


import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public abstract class AbstractDataSource implements DataSource {

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException("AbstractDataSource is not a wrapper.");
    }

    @Override
    public Connection getConnection(String uname, String passwd) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintWriter getLogWriter() {
        return _logWriter;
    }

    @Override
    public int getLoginTimeout() {
        throw new UnsupportedOperationException("Login timeout is not supported.");
    }

    @Override
    public void setLoginTimeout(int seconds) {
        throw new UnsupportedOperationException("Login timeout is not supported.");
    }

    @Override
    public void setLogWriter(PrintWriter out) {
        _logWriter = out;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("no parent logger");
    }

    public Connection openConnection() throws SQLException {
        return getConnection();
    }

    protected PrintWriter _logWriter = null;
}
