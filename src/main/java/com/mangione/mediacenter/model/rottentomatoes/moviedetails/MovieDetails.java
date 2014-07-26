package com.mangione.mediacenter.model.rottentomatoes.moviedetails;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mangione.mediacenter.model.rottentomatoes.MoviePosters;

public class MovieDetails {
    private final String title;
    private final String year;
    private final String[] genres;
    private final MoviePosters posters;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    public static MovieDetails fromJson(String json){
        return GSON.fromJson(json, MovieDetails.class);
    }


    public MovieDetails(String title, String year, String[] genres, MoviePosters posters) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.posters = posters;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String[] getGenres() {
        return genres;
    }

    public MoviePosters getPosters() {
        return posters;
    }
}
