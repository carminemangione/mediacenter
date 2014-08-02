package com.mangione.mediacenter.view.rottentomatoes.moviedetails;

import com.mangione.mediacenter.model.rottentomatoes.RottenTomatoesResource;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.MovieDetails;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.Synopsis;
import com.mangione.mediacenter.view.rottentomatoes.RottenTomatoesControllerInterface;
import com.sun.jersey.api.client.WebResource;

import javax.swing.*;

public class MovieDetailsController implements RottenTomatoesControllerInterface {

    private final MovieDetailsPanel movieDetailsPanel;

    public MovieDetailsController(String movieDetailsLink) throws Exception {
        final WebResource resource = new RottenTomatoesResource(movieDetailsLink).getResource();
        final MovieDetails movieDetails = MovieDetails.fromJson(resource.get(String.class));
        final String synopsis = Synopsis.fromWeblink(movieDetails.getLinks().getAlternate()).getSynopsis();

        movieDetailsPanel = new MovieDetailsPanel(movieDetails, synopsis);
    }

    @Override
    public JPanel getPanel() {
        return movieDetailsPanel;
    }
}
