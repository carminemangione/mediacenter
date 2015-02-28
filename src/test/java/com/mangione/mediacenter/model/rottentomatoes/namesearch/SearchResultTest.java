package com.mangione.mediacenter.model.rottentomatoes.namesearch;

import com.mangione.mediacenter.model.MovieLinks;
import com.mangione.mediacenter.model.rottentomatoes.MoviePosters;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SearchResultTest {

    @Test
    public void reconstituteFromJSON() throws Exception {
        final String json = IOUtils.toString(
                SearchResultTest.class.getClassLoader().getResourceAsStream("search_result.json"));
        final SearchResult searchResult = SearchResult.fromJson(json);
        assertEquals(2, searchResult.getTotal());
        final RTMovie[] movies = searchResult.getMovies();
        assertEquals(2, movies.length);
        final RTMovie underOneRoof = movies[0];
        assertEquals("17414", underOneRoof.getId());
        assertEquals("Under One Roof", underOneRoof.getTitle());
        assertEquals("2002", underOneRoof.getYear());
        final MoviePosters posters = underOneRoof.getPosters();
        assertEquals("http://content9.flixster.com/movie/10/91/67/10916739_pro.jpg", posters.getProfile());
        final MovieLinks links = underOneRoof.getLinks();
        assertEquals("http://api.rottentomatoes.com/api/public/v1.0/movies/17414.json", links.getSelf());

    }



    @Test
    public void reconstituteZedSearchFromJSON() throws Exception {
        final String json = IOUtils.toString(
                SearchResultTest.class.getClassLoader().getResourceAsStream("search_zed.json"));
        final SearchResult searchResult = SearchResult.fromJson(json);
        assertEquals(9, searchResult.getTotal());

    }
}
