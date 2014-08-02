package com.mangione.mediacenter.model.rottentomatoes.moviedetails;

public class Ratings {

    private final String critics_score;
    private final String audience_rating;
    private final String audience_score;

    public Ratings(String critics_score, String audience_rating, String audience_score) {
        this.critics_score = critics_score;
        this.audience_rating = audience_rating;
        this.audience_score = audience_score;
    }

    public String getCriticsScore() {
        return critics_score;
    }

    public String getAudienceRating() {
        return audience_rating;
    }

    public String getAudienceScore() {
        return audience_score;
    }
}
