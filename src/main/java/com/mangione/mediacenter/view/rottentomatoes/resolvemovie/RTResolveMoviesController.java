package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTSearchResult;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RottenTomatoesSearch;
import com.mangione.mediacenter.view.SharedConstants;

import javax.swing.*;

public class RTResolveMoviesController {

    private JPanel resolveMoviesPanel;

    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame();
        final RTResolveMoviesController rtResolveMoviesController = new RTResolveMoviesController("under one roof");
        frame.setContentPane(rtResolveMoviesController.resolveMoviesPanel);
        frame.setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public RTResolveMoviesController(String movieName) throws Exception {
        final RTSearchResult searchResult = new RottenTomatoesSearch(movieName).getSearchResult();
        final RTMovie[] movies = searchResult.getMovies();
        resolveMoviesPanel = new RTResolveMoviesPanel(movies);
    }

    public JPanel getResolveMoviesPanel() {
        return resolveMoviesPanel;
    }
}

