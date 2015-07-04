package com.mangione.mediacenter.model;

import com.mangione.common.database.*;
import com.mangione.mediacenter.model.database.MediaCenterDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VideoDirectories {
    private static final VideoDirectories instance;
    static {
        try {
            instance = new VideoDirectories();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private static final String TABLE_NAME = "moviedirectories";
    private static final String SCHEMA_NAME = "mediacenter";
    private static final String QUALIFIED_TABLE_NAME = SCHEMA_NAME + "." + TABLE_NAME;
    private final DerbyConnectionFactory derbyConnectionFactory;

    private VideoDirectories() throws Exception {


        boolean tableExists;
        derbyConnectionFactory = MediaCenterDataSource.get();
        tableExists = derbyConnectionFactory.doesTableExist(SCHEMA_NAME, TABLE_NAME);

        if (!tableExists) {
            createDBTable();
        }

        String deleteDirectories = System.getProperty("deleteDirectories", "false");
        if ("true".equals(deleteDirectories)) {
            clearDirectoriesTable();
        }
    }

    private void clearDirectoriesTable() throws SQLException {
        new UpdateQuery(derbyConnectionFactory) {

            @Override
            protected void bindQueryParameters(PreparedStatement ps) throws SQLException {

            }

            @Override
            protected String getUpdateQuery() {
                return "DELETE * FROM " + QUALIFIED_TABLE_NAME;
            }
        };
    }

    private void createDBTable() {
        try (Connection connection = derbyConnectionFactory.getConnection()) {
            new UpdateQuery(connection) {

                @Override
                protected String getUpdateQuery() {
                    return "CREATE TABLE " + QUALIFIED_TABLE_NAME +
                            " (directory VARCHAR(256), " +
                            " PRIMARY KEY (directory))";
                }
                @Override
                protected void bindQueryParameters(PreparedStatement ps) throws SQLException {

                }
            };
                        
        } catch (Exception e) {
            e.printStackTrace();
        } 

    }

    public static VideoDirectories getInstance() {
        return instance;
    }

    public String[] getVideoDirectories()  {
        final List<String> videoDirectories = new ArrayList<>();
        try {
            new Query(derbyConnectionFactory) {

                @Override
                protected String getQueryString() throws SQLException {
                    return "SELECT directory FROM " + QUALIFIED_TABLE_NAME;
                }

                @Override
                protected void bindQueryParameters(PreparedStatement ps) throws SQLException {

                }

                @Override
                protected void processResults(ResultSet rs) throws SQLException {
                    while (rs.next()) {
                        videoDirectories.add(rs.getString("directory"));
                    }
                }
            };
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return videoDirectories.toArray(new String[videoDirectories.size()]);
    }

    public void setDirectories(String[] directories)  {

        try {
            new Transaction(derbyConnectionFactory) {

                @Override
                protected void doTransaction(Connection connection) throws SQLException {
                    for (String directory : directories) {
                        try {
                            new InsertQuery(derbyConnectionFactory.getConnection()) {
                                @Override
                                protected void bindQueryParameters(PreparedStatement ps) throws SQLException {
                                    ps.setString(1, directory);
                                }

                                @Override
                                protected String getInsertQuery() {
                                    return "INSERT INTO " + QUALIFIED_TABLE_NAME + " VALUES (?)";
                                }
                            };
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            };
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() {

        try (Connection connection = derbyConnectionFactory.getConnection()) {
            new UpdateQuery(connection) {

                @Override
                protected String getUpdateQuery() {
                    return "DELETE FROM  " + QUALIFIED_TABLE_NAME;
                }
                @Override
                protected void bindQueryParameters(PreparedStatement ps) throws SQLException {

                }
            };

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
