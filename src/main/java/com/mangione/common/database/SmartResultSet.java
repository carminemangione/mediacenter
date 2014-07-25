package com.mangione.common.database;

import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class SmartResultSet implements AutoCloseable {

    private static final Object[] EMPTY_ARRAY = new Object[0];
    private static final Long[] EMPTY_LONG_ARRAY = new Long[0];
    private static final Boolean[] EMPTY_BOOLEAN_ARRAY = new Boolean[0];
    private final ResultSet resultSet;

    public SmartResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public String getString(String columnLabel) throws SQLException {
        return resultSet.getString(columnLabel);
    }

    public boolean getBoolean(String columnLabel) throws SQLException {
        return resultSet.getBoolean(columnLabel);
    }

    public int getInt(String columnLabel) throws SQLException {
        return resultSet.getInt(columnLabel);
    }

    public long getLong(String columnLabel) throws SQLException {
        return resultSet.getLong(columnLabel);
    }

    public double getDouble(String columnLabel) throws SQLException {
        return resultSet.getDouble(columnLabel);
    }

    public Date getDate(String columnLabel) throws SQLException {
        return resultSet.getDate(columnLabel);
    }

    public Time getTime(String columnLabel) throws SQLException {
        return resultSet.getTime(columnLabel);
    }

    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return resultSet.getTimestamp(columnLabel);
    }

    public Array getArray(String columnLabel) throws SQLException {
        return resultSet.getArray(columnLabel);
    }

    public boolean next() throws SQLException {
        return resultSet.next();
    }

    @Override
    public void close() throws SQLException {
        resultSet.close();
    }

    public Double getNullableDouble(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : resultSet.getDouble(columnLabel);
    }

    public Long getNullableLong(String columnLabel) throws SQLException {
        return isNull(columnLabel) ? null : resultSet.getLong(columnLabel);
    }

    private boolean isNull(String columnLabel) throws SQLException {
        Object object = resultSet.getObject(columnLabel);
        return object == null;
    }

    public Long[] getLongArray(String columnLabel) throws SQLException {
        Array array = resultSet.getArray(columnLabel);
        return array == null ? EMPTY_LONG_ARRAY : (Long[]) array.getArray();
    }

    public Long[] getNullableLongArray(String columnLabel) throws SQLException {
        Array array = resultSet.getArray(columnLabel);
        return array == null ? null : (Long[]) array.getArray();
    }

    public Boolean[] getBooleanArray(String columnLabel) throws SQLException {
        Array array = resultSet.getArray(columnLabel);
        return array == null ? EMPTY_BOOLEAN_ARRAY : (Boolean[]) array.getArray();
    }

    public Boolean[] getNullableBooleanArray(String columnLabel) throws SQLException {
        Array array = resultSet.getArray(columnLabel);
        return array == null ? null : (Boolean[]) array.getArray();
    }

    public <ARRAY_TYPE> ARRAY_TYPE getArray(Class<ARRAY_TYPE> t, String columnLabel) throws SQLException {
        Array array = resultSet.getArray(columnLabel);
        return array == null ? (ARRAY_TYPE) EMPTY_ARRAY : t.cast(array.getArray());
    }

    public <ARRAY_TYPE> ARRAY_TYPE getNullableArray(Class<ARRAY_TYPE> t, String columnLabel) throws SQLException {
        Array array = resultSet.getArray(columnLabel);
        return array == null ? null : t.cast(array.getArray());
    }
}
