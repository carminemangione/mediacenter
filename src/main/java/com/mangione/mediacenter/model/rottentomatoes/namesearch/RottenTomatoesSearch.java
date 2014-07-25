package com.mangione.mediacenter.model.rottentomatoes.namesearch;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;


public class RottenTomatoesSearch {

    private static final String API_KEY = "2ufc48xdsqutqtwrrzpg8rcd";
    private RTSearchResult searchResult;


    public RottenTomatoesSearch(String movieName) {
        WebResource resource = Client.create()
                .resource("http://api.rottentomatoes.com/api/public/v1.0/movies.json")
                .queryParam("apikey", API_KEY).queryParam("q", movieName);

        searchResult = RTSearchResult.fromJson(resource.get(String.class));
        


    }

    public RTSearchResult getSearchResult() {
        return searchResult;
    }
}
