package com.mangione.mediacenter.model.rottentomatoes.namesearch;

import com.mangione.mediacenter.model.rottentomatoes.RottenTomatoesResource;
import com.sun.jersey.api.client.WebResource;


public class RottenTomatoesSearch {

    private SearchResult searchResult;

    public RottenTomatoesSearch(String movieName) {
        WebResource resource = new RottenTomatoesResource("http://api.rottentomatoes.com/api/public/v1.0/movies.json").
                getResource().queryParam("q", movieName);
        searchResult = SearchResult.fromJson(resource.get(String.class));
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }
}
