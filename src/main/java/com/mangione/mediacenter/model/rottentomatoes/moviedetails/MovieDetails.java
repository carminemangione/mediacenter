package com.mangione.mediacenter.model.rottentomatoes.moviedetails;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mangione.mediacenter.model.MovieLinks;
import com.mangione.mediacenter.model.rottentomatoes.MoviePosters;

import java.util.Arrays;

public class MovieDetails {
    private final String title;
    private final String year;
    private final String[] genres;
    private final MoviePosters posters;
    private final MovieLinks links;
    private final Ratings ratings;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    public static MovieDetails fromJson(String json){
        return GSON.fromJson(json, MovieDetails.class);
    }


    public MovieDetails(String title, String year, String[] genres, MoviePosters posters, MovieLinks links, Ratings ratings) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.posters = posters;
        this.links = links;
        this.ratings = ratings;
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

    public MovieLinks getLinks() {
        return links;
    }

    public Ratings getRatings() {
        return ratings;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieDetails)) return false;

        MovieDetails that = (MovieDetails) o;

        if (links != null ? !links.equals(that.links) : that.links != null) return false;
        if (posters != null ? !posters.equals(that.posters) : that.posters != null) return false;
        if (ratings != null ? !ratings.equals(that.ratings) : that.ratings != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (year != null ? !year.equals(that.year) : that.year != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (genres != null ? Arrays.hashCode(genres) : 0);
        result = 31 * result + (posters != null ? posters.hashCode() : 0);
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (ratings != null ? ratings.hashCode() : 0);
        return result;
    }
}
