package com.mangione.mediacenter.model.database;

import com.mangione.common.applicationdata.ApplicationDataLocation;
import com.mangione.common.database.AbstractDataSource;
import com.mangione.common.database.DerbyConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class MediaCenterDataSource extends AbstractDataSource {

    private static AbstractDataSource INSTANCE;

    static {
        try {
            new MediaCenterDataSource(new DerbyConnectionFactory(new ApplicationDataLocation("MediaCenter").getApplicationDataLocation(), "MediaCenter"));

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public MediaCenterDataSource (AbstractDataSource dataSource) {
        INSTANCE = dataSource;
    }


    public static AbstractDataSource get() {
        return INSTANCE;
    }



    @Override
    public Connection getConnection() throws SQLException {
        return INSTANCE.getConnection();
    }
}
