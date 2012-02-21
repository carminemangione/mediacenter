package com.mangione.mediacenter.model.rottentomatoes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RTSearchResult {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final int total;
    private final RTMovie[] movies;

    public static RTSearchResult fromJson(String json){
        return GSON.fromJson(json, RTSearchResult.class);
    }

    public RTSearchResult(int total, RTMovie[] movies) {
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
