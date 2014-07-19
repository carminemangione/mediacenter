package com.mangione.mediacenter.model.rottentomatoes.namesearch;

public class RTMovie {
    private final String id;
    private final String title;
    private final Long year;
    private final RTPosters posters;
    private final RTMovieLinks links;

    public RTMovie(String id, String title, Long year, RTPosters posters, RTMovieLinks links) {
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

    public Long getYear() {
        return year;
    }

    public RTPosters getPosters() {
        return posters;
    }

    @Override
    public String toString() {
        return title + " (" + year + ")";
    }

    public RTMovieLinks getLinks() {
        return links;
    }
}
