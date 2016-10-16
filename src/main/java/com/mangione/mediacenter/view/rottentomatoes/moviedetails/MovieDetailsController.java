package com.mangione.mediacenter.view.rottentomatoes.moviedetails;

import com.mangione.mediacenter.model.rottentomatoes.RottenTomatoesResource;
import com.mangione.mediacenter.model.rottentomatoes.database.ArchivedMovies;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.DetailsAndSynopsis;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.MovieDetails;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.Synopsis;
import com.mangione.mediacenter.view.rottentomatoes.RottenTomatoesControllerInterface;
import com.sun.jersey.api.client.WebResource;

import javax.swing.*;

public class MovieDetailsController implements RottenTomatoesControllerInterface {

    private final MovieDetailsPanel movieDetailsPanel;
    private final DetailsAndSynopsis detailsAndSynopsis;

    public MovieDetailsController(String currentMovieInSearchPath, String movieDetailsLink) throws Exception {
        this(loadDetailsAndSynopsis(movieDetailsLink));
        ArchivedMovies.getInstance().addMovieURL(currentMovieInSearchPath, movieDetailsLink, detailsAndSynopsis);
    }

    public MovieDetailsController(DetailsAndSynopsis detailsAndSynopsis) throws Exception {
        this.detailsAndSynopsis = detailsAndSynopsis;
        movieDetailsPanel = new MovieDetailsPanel(detailsAndSynopsis);
    }

    @Override
    public JPanel getPanel() {
        return movieDetailsPanel;
    }

    private static DetailsAndSynopsis loadDetailsAndSynopsis(String movieDetailsLink) {
        final WebResource resource = new RottenTomatoesResource(movieDetailsLink).getResource();
        MovieDetails movieDetails = MovieDetails.fromJson(resource.get(String.class));
        String alternate = movieDetails.getLinks().getAlternate();
        if (!alternate.contains("https"))
            alternate = alternate.replace("http", "https");
        Synopsis synopsis = Synopsis.fromWebLink(alternate);
        return new DetailsAndSynopsis(movieDetails, synopsis);
    }

}
