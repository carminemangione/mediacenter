package com.mangione.mediacenter.model.rottentomatoes;

public class RTMovie {
    private final String id;
    private final String title;
    private final String year;
    private final RTPosters posters;

    public RTMovie(String id, String title, String year, RTPosters posters) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.posters = posters;
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

    public RTPosters getPosters() {
        return posters;
    }
}
