package com.mangione.mediacenter.model.rottentomatoes.namesearch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SearchResult {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final int total;
    private final RTMovie[] movies;

    public static SearchResult fromJson(String json){
        return GSON.fromJson(json, SearchResult.class);
    }

    public SearchResult(int total, RTMovie[] movies) {
        this.total = total;
        this.movies = movies;
    }

    public int getTotal() {
        return total;
    }

    public RTMovie[] getMovies() {
        return movies;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
