package com.mangione.mediacenter.model.rottentomatoes.database;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ArchivedMoviesTest {

    private static final String APPLICATION_DATA_DIRECTORY = "/Application/FileLocation/";
    private String tempApplicationDataDirectory;
    private String tempDirectory;

    @Before
    public void setUp() throws Exception {
        File tempFile = File.createTempFile("ArchivedMovieTest", "tst");
        tempDirectory = tempFile.getParent();
        tempApplicationDataDirectory = tempDirectory + APPLICATION_DATA_DIRECTORY;
        assertTrue(tempFile.delete());
    }

    @After
    public void cleanup() throws Exception {
        if (tempDirectory != null) {
            FileUtils.deleteDirectory(new File(tempApplicationDataDirectory));
        }
    }


    @Test
    public void noFileCreatesDirectoriesAndFile() throws Exception {
        File applicationDirectory = new File(tempApplicationDataDirectory);
        assertFalse(applicationDirectory.exists());

        new ArchivedMovies(tempApplicationDataDirectory, "ArcivedMoviesTest");

        assertTrue(applicationDirectory.exists());
    }

    @Test
    public void storeAndRetrieveMovieUrl() throws Exception {
        String movieURL = "http://api.rottentomatoes.com/api/public/v1.0/movies/576297406.json";
        String movieName = "UNDER_ONE_ROOF";

        final ArchivedMovies archivedMovies = new ArchivedMovies(tempApplicationDataDirectory, "ArchivedMoviesTest");

        assertNull(archivedMovies.getMovieURL(movieName));
        archivedMovies.addMovieURL(movieName, movieURL);

        final ArchivedMovies newArchivedMovies = new ArchivedMovies(tempApplicationDataDirectory, "ArchivedMoviesTest");
        assertEquals(movieURL, newArchivedMovies.getMovieURL(movieName));

    }
}
