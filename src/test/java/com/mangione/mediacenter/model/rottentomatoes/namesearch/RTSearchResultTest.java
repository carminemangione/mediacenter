package com.mangione.mediacenter.model.rottentomatoes.namesearch;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RTSearchResultTest {

    @Test
    public void reconstituteFromJSON() throws Exception {
        final String json = IOUtils.toString(
                RTSearchResultTest.class.getClassLoader().getResourceAsStream("search_result.json"));
        final RTSearchResult rtSearchResult = RTSearchResult.fromJson(json);
        assertEquals(2, rtSearchResult.getTotal());
        final RTMovie[] movies = rtSearchResult.getMovies();
        assertEquals(2, movies.length);
        final RTMovie underOneRoof = movies[0];
        assertEquals("17414", underOneRoof.getId());
        assertEquals("Under One Roof", underOneRoof.getTitle());
        assertEquals(new Long(2002), underOneRoof.getYear());
        final RTPosters posters = underOneRoof.getPosters();
        assertEquals("http://content9.flixster.com/movie/10/91/67/10916739_pro.jpg", posters.getProfile());
        assertEquals("http://content9.flixster.com/movie/10/91/67/10916739_mob.jpg", posters.getThumbnail());
        final RTMovieLinks links = underOneRoof.getLinks();
        assertEquals("http://api.rottentomatoes.com/api/public/v1.0/movies/17414.json", links.getSelf());

    }
}
