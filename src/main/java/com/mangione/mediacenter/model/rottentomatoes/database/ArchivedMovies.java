package com.mangione.mediacenter.model.rottentomatoes.database;

import com.mangione.common.database.*;
import com.mangione.mediacenter.model.database.MediaCenterDataSource;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.DetailsAndSynopsis;

import java.io.*;
import java.sql.*;

public class ArchivedMovies {
    private static final ArchivedMovies INSTANCE;
    static {
        try {
            INSTANCE = new ArchivedMovies();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArchivedMovies getInstance() {
        return INSTANCE;
    }


    private final AbstractDataSource derbyConnectionFactory;


    public ArchivedMovies(AbstractDataSource dataSource) throws Exception {
        derbyConnectionFactory = dataSource;

        boolean tableExists;
        tableExists = doesTableExist();

        if (!tableExists) {
            createDBTable();
        }
    }

    public ArchivedMovies() throws Exception {
        this(MediaCenterDataSource.get());
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

    public DetailsAndSynopsis getMovie(String moviePath) throws SQLException {
        final DetailsAndSynopsis[] movie = {null};
        new Query(derbyConnectionFactory) {

            @Override
            protected String getQueryString() throws SQLException {
                return "SELECT rtmovie FROM mediacenter.rtmovies WHERE moviePath = ?";
            }

            @Override
            protected void bindQueryParameters(PreparedStatement ps) throws SQLException {
                ps.setString(1, moviePath);
            }

            @Override
            protected void processResults(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    final byte[] rtbytes = rs.getBytes("rtmovie");

                    ByteArrayInputStream bais = new ByteArrayInputStream(rtbytes);
                    try {
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        movie[0] = (DetailsAndSynopsis) ois.readObject();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        return movie[0];
    }

    private void createDBTable()  {
        try {
            new Transaction(derbyConnectionFactory) {
                @Override
                protected void doTransaction(Connection connection) throws SQLException {
                    new UpdateQuery(connection) {

                        @Override
                        protected String getUpdateQuery() {
                            return "CREATE TABLE mediacenter.rtmovies " +
                                    "(moviePath VARCHAR(256), " +
                                    "   movieURL VARCHAR(256), " +
                                    " rtmovie BLOB(16M), " +
                                    " PRIMARY KEY (moviePath))";
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

    public void addMovieURL(String moviePath, String movieURL, DetailsAndSynopsis detailsAndSynopsis) throws SQLException {
        new Transaction(derbyConnectionFactory) {
            @Override
            protected void doTransaction(Connection connection) throws SQLException {
                new InsertQuery(connection) {
                    @Override
                    protected String getInsertQuery() {
                        return "INSERT INTO mediacenter.rtmovies (moviePath, movieURL, rtmovie) VALUES (?, ?, ?)";
                    }

                    @Override
                    protected void bindQueryParameters(PreparedStatement ps) throws SQLException {
                        ps.setString(1, moviePath);
                        ps.setString(2, movieURL);

                        try {
                            final ByteArrayOutputStream out = new ByteArrayOutputStream();
                            ObjectOutputStream oos = new ObjectOutputStream(out);
                            oos.writeObject(detailsAndSynopsis);
                            ByteArrayInputStream bais = new ByteArrayInputStream(out.toByteArray());
                            ps.setBinaryStream(3, bais);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                };
            }
        };
    }
}
