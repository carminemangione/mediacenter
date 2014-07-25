package com.mangione.mediacenter.model.rottentomatoes.database;

import com.mangione.common.database.*;

import java.sql.*;

public class ArchivedMovies {

    private final DerbyConnectionFactory derbyConnectionFactory;

    public ArchivedMovies(String applicationDataDirectory, String dbname) throws Exception {

        derbyConnectionFactory = new DerbyConnectionFactory(applicationDataDirectory, dbname);
        boolean tableExists;
        tableExists = doesTableExist();

        if (!tableExists) {
            createDBTable();
        }
    }

    private boolean doesTableExist() throws SQLException {
        boolean tableExists;
        try (Connection connection = derbyConnectionFactory.getConnection()) {
            final DatabaseMetaData metaData = connection.getMetaData();
            final ResultSet rs = metaData.getSuperTables(null, "MEDIACENTER", "*");
            tableExists = rs.next();
        }
        return tableExists;
    }

    private void createDBTable()  {
        try {
            new Transaction(derbyConnectionFactory) {
                @Override
                protected void doTransaction(Connection connection) throws SQLException {
                    new UpdateQuery(connection) {

                        @Override
                        protected String getUpdateQuery() {
                            return "CREATE TABLE mediacenter.movie_links (movieName VARCHAR(128), movieURL VARCHAR(256), PRIMARY KEY (movieName))";
                        }

                        @Override
                        protected void bindQueryParameters(PreparedStatement ps) throws SQLException {

                        }
                    };
                }
            };
        } catch (SQLException ignored) {

        }
    }

    public String getMovieURL(String movieName) throws SQLException {
        final String[] movieURL = {null};
        new Query(derbyConnectionFactory) {

            @Override
            protected String getQueryString() throws SQLException {
                return "SELECT movieURL FROM mediacenter.movie_links WHERE movieName = ?";
            }

            @Override
            protected void bindQueryParameters(PreparedStatement ps) throws SQLException {
                ps.setString(1, movieName);
            }

            @Override
            protected void processResults(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    movieURL[0] = rs.getString("movieURL");
                }
            }
        };

        return movieURL[0];
    }

    public void addMovieURL(String movieName, String movieURL) throws SQLException {
        new Transaction(derbyConnectionFactory) {
            @Override
            protected void doTransaction(Connection connection) throws SQLException {
                new InsertQuery(connection) {
                    @Override
                    protected String getInsertQuery() {
                        return "INSERT INTO mediacenter.movie_links (movieName, movieURL) VALUES (?, ?)";
                    }

                    @Override
                    protected void bindQueryParameters(PreparedStatement ps) throws SQLException {
                        ps.setString(1, movieName);
                        ps.setString(2, movieURL);
                    }
                };
            }
        };
    }
}
