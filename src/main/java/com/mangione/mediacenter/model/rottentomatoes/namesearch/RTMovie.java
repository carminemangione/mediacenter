package com.mangione.mediacenter.model.rottentomatoes.namesearch;

import com.mangione.mediacenter.model.MovieLinks;
import com.mangione.mediacenter.model.rottentomatoes.MoviePosters;

import java.io.Serializable;

public class RTMovie implements Serializable {
    private final String id;
    private final String title;
    private final String year;
    private final MoviePosters posters;
    private final MovieLinks links;

    public RTMovie(String id, String title, String year, MoviePosters posters, MovieLinks links) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.posters = posters;
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public MoviePosters getPosters() {
        return posters;
    }

    @Override
    public String toString() {
        return title + " (" + year + ")";
    }

    public MovieLinks getLinks() {
        return links;
    }
}
