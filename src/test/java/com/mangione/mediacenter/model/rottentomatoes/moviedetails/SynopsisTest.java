package com.mangione.mediacenter.model.rottentomatoes.moviedetails;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class SynopsisTest {
    @Test
    public void extractsSynopsisFromHTML() throws IOException {
        final String webpage = IOUtils.toString(SynopsisTest.class.getClassLoader().getResourceAsStream("movie_web_page.html"));
        Synopsis synopsis = new Synopsis(webpage);
        assertTrue(synopsis.getSynopsis().startsWith("A young, gay"));
        assertTrue(synopsis.getSynopsis().endsWith("~ Michael Hastings, Rovi"));
        final String lineChangeConvertedToSpace = "hunky border";
        assertTrue(synopsis.getSynopsis().contains(lineChangeConvertedToSpace));
        final String moreAddedCorrectly = "clandestine";
        assertTrue(synopsis.getSynopsis().contains(moreAddedCorrectly));
    }


    @Test
    public void extractsSynopsisWithMissingMoreFromHTML() throws IOException {
        final String webpage = IOUtils.toString(SynopsisTest.class.getClassLoader().getResourceAsStream("movie_web_page_missing_more.html"));
        Synopsis synopsis = new Synopsis(webpage);
        assertTrue(synopsis.getSynopsis().startsWith("A young, gay"));
        assertTrue(synopsis.getSynopsis().endsWith("begin a"));
        final String lineChangeConvertedToSpace = "hunky border";
        assertTrue(synopsis.getSynopsis().contains(lineChangeConvertedToSpace));
    }


    @Test
    public void ethanMaoIsDifferent() throws Exception {

        final String webpage = IOUtils.toString(SynopsisTest.class.getClassLoader().getResourceAsStream("ethanmao.html"));
        Synopsis synopsis = new Synopsis(webpage);
        assertTrue(synopsis.getSynopsis().startsWith("The product of a dysfunctional family takes unusual"));
        assertTrue(synopsis.getSynopsis().endsWith(" filmmaker Quentin Lee."));
        final String lineChangeConvertedToSpace = "house. Ethan";
        assertTrue(synopsis.getSynopsis().contains(lineChangeConvertedToSpace));
    }

}
