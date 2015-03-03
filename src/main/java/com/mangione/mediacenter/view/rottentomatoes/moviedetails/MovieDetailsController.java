package com.mangione.mediacenter.view.rottentomatoes.moviedetails;

import com.mangione.mediacenter.model.rottentomatoes.RottenTomatoesResource;
import com.mangione.mediacenter.model.rottentomatoes.database.ArchivedMovies;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.DetailsAndSynopsis;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.MovieDetails;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.Synopsis;
import com.mangione.mediacenter.view.rottentomatoes.RTMainController;
import com.mangione.mediacenter.view.rottentomatoes.RottenTomatoesControllerInterface;
import com.sun.jersey.api.client.WebResource;

import javax.swing.*;

public class MovieDetailsController implements RottenTomatoesControllerInterface {

    private final MovieDetailsPanel movieDetailsPanel;
    private final RTMainController rtMainController;
    private final DetailsAndSynopsis detailsAndSynopsis;

    public MovieDetailsController(RTMainController rtMainController, String currentMovieInSearchPath, String movieDetailsLink) throws Exception {
        this(rtMainController, loadDetailsAndSynopsis(movieDetailsLink));
        ArchivedMovies.getInstance().addMovieURL(currentMovieInSearchPath, movieDetailsLink, detailsAndSynopsis);
    }

    public MovieDetailsController(RTMainController rtMainController, DetailsAndSynopsis detailsAndSynopsis) throws Exception {
        this.rtMainController = rtMainController;
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
        Synopsis synopsis = Synopsis.fromWebLink(movieDetails.getLinks().getAlternate());
        return new DetailsAndSynopsis(movieDetails, synopsis);
    }

}
