package com.mangione.mediacenter.model.rottentomatoes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * User: carminemangione
 * Date: Jul 24, 2010
 * Time: 4:25:31 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class RottenTomatoesSearch {
    public static void main(String[] args) throws Exception {
        String searchUrl = "http://www.rottentomatoes.com/search/full_search.php?search=under%20one%20roof";
        new RottenTomatoesSearch(searchUrl);


    }

    public RottenTomatoesSearch(String searchUrl) throws Exception {
        List<String> movieLinks = retrieveLinksFromUrl(searchUrl);

        for (String movieLink : movieLinks) {
            System.out.println("Searching link: " + movieLink);
            getDetailsForMovie("http://www.rottentomatoes.com" + movieLink);
        }


    }

    private void getDetailsForMovie(String movieUrl) throws Exception{
        URL yahoo = new URL(movieUrl);
        URLConnection yc = yahoo.openConnection();

        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
    }

    private List<String> retrieveLinksFromUrl(String searchUrl) throws IOException {
        List<String> movieLinks = new ArrayList<String>();
        URL yahoo = new URL(searchUrl);
        URLConnection yc = yahoo.openConnection();

        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

        boolean foundMovieCaption = false;
        while ((inputLine = in.readLine()) != null)  {
            foundMovieCaption = foundMovieCaption || inputLine.contains("<caption>Movies</caption>");
            if (foundMovieCaption) {
                if (inputLine.contains("href")) {
                    String beginningOfLink = inputLine.substring(inputLine.indexOf("href") + 6);
                    String link = beginningOfLink.substring(0, beginningOfLink.indexOf("/\""));
                    movieLinks.add(link);
                } else {
                    if (inputLine.contains("/table"))
                        break;
                }

            }
        }
        return movieLinks;
    }
}
