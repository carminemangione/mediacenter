package com.mangione.mediacenter.model.rottentomatoes.moviedetails;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MovieDetailsTest {
    @Test
    public void loadDetails() throws IOException {
        final String json = IOUtils.toString(SynopsisTest.class.getClassLoader().getResourceAsStream("MovieDetails.json"));

        MovieDetails movieDetails = MovieDetails.fromJson(json);
        assertEquals(2, movieDetails.getGenres().length);
        assertEquals("Gay & Lesbian", movieDetails.getGenres()[0]);
        assertEquals("Comedy", movieDetails.getGenres()[1]);
    }
}
