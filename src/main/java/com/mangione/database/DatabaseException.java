package com.mangione.database;

import java.sql.SQLException;

public class DatabaseException extends SQLException {
    public DatabaseException(Throwable e) {
        super(e);
    }
}
