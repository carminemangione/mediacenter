package com.mangione.mediacenter.model.rottentomatoes.moviedetails;

import java.io.Serializable;

public class DetailsAndSynopsis implements Serializable {
    private final MovieDetails movieDetails;
    private final Synopsis synopsis;

    public DetailsAndSynopsis(MovieDetails movieDetails, Synopsis synopsis) {
        this.movieDetails = movieDetails;
        this.synopsis = synopsis;
    }

    public MovieDetails getMovieDetails() {
        return movieDetails;
    }

    public Synopsis getSynopsis() {
        return synopsis;
    }
}
