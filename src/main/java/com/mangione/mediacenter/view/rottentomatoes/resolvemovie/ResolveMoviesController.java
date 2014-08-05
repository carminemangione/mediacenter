package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RottenTomatoesSearch;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.SearchResult;
import com.mangione.mediacenter.view.rottentomatoes.RottenTomatoesControllerInterface;

import javax.swing.*;

public class ResolveMoviesController implements RottenTomatoesControllerInterface {

    private final MovieResolvedListener rtMainController;
    private JPanel resolveMoviesPanel;

    public ResolveMoviesController(String movieName, MovieResolvedListener rtMainController) throws Exception {
        this.rtMainController = rtMainController;
        final SearchResult searchResult = new RottenTomatoesSearch(movieName).getSearchResult();
        RTMovie[] movies = searchResult.getMovies();
        resolveMoviesPanel = new ResolveMoviesPanel(movies, this);
    }

    public void resolveMovieSelected(RTMovie selectedMovie) {
        rtMainController.resolvedMovieSelected(selectedMovie);
    }

    @Override
    public JPanel getPanel() {
        return resolveMoviesPanel;
    }
}

