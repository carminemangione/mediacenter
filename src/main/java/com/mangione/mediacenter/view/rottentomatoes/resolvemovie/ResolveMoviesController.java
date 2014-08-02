package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RottenTomatoesSearch;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.SearchResult;
import com.mangione.mediacenter.view.rottentomatoes.RottenTomatoesControllerInterface;

import javax.swing.*;

public class ResolveMoviesController implements RottenTomatoesControllerInterface {

    private JPanel resolveMoviesPanel;

    public ResolveMoviesController(String movieName) throws Exception {
        final SearchResult searchResult = new RottenTomatoesSearch(movieName).getSearchResult();
        final RTMovie[] movies = searchResult.getMovies();
        resolveMoviesPanel = new ResolveMoviesPanel(movies);
    }

    @Override
    public JPanel getPanel() {
        return resolveMoviesPanel;
    }
}

