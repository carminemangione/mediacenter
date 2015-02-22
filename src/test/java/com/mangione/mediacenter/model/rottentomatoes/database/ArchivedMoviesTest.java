package com.mangione.mediacenter.model.rottentomatoes.database;

import com.mangione.common.applicationdata.ApplicationDataLocation;
import com.mangione.common.database.DerbyConnectionFactory;
import com.mangione.mediacenter.model.MovieLinks;
import com.mangione.mediacenter.model.rottentomatoes.MoviePosters;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.DetailsAndSynopsis;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.MovieDetails;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.Ratings;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.Synopsis;
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

        new ArchivedMovies( new DerbyConnectionFactory(new ApplicationDataLocation("MediaCenter").getApplicationDataLocation(), "MediaCenterTest"));

        assertTrue(applicationDirectory.exists());
    }

    @Test
    public void storeAndRetrieveMovieUrl() throws Exception {
        String moviePath = "UNDER_ONE_ROOF";

        final ArchivedMovies archivedMovies = new ArchivedMovies(new DerbyConnectionFactory(new ApplicationDataLocation("MediaCenterTest").getApplicationDataLocation(), "MediaCenterTest"));
        MovieDetails details = new MovieDetails(10, "Hello", "1999", new String[]{}, new MoviePosters("thumb", "prof", "orig"),
                new MovieLinks("self", "alternate"), new Ratings("", null, null));
        DetailsAndSynopsis detailsAndSynopsis = new DetailsAndSynopsis(details, new Synopsis("Hi there"));

        assertNull(archivedMovies.getMovie(moviePath));
        archivedMovies.addMovieURL(moviePath, "url", detailsAndSynopsis);

        final ArchivedMovies newArchivedMovies = new ArchivedMovies(new DerbyConnectionFactory(new ApplicationDataLocation("MediaCenterTest").getApplicationDataLocation(), "MediaCenterTest"));
        assertEquals(10, newArchivedMovies.getMovie(moviePath).getMovieDetails().getId());

    }
}
