package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTSearchResult;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RottenTomatoesSearch;

import javax.swing.*;

/**
 * User: carminemangione
 * Date: Dec 28, 2009
 * Time: 9:34:16 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class RTResolveMoviesController {

    private JPanel resolveMoviesPanel;

    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame();
        final RTResolveMoviesController rtResolveMoviesController = new RTResolveMoviesController("under one roof");
        frame.setContentPane(rtResolveMoviesController.resolveMoviesPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public RTResolveMoviesController(String movieName) throws Exception {
        final RTSearchResult searchResult = new RottenTomatoesSearch(movieName).getSearchResult();
        final RTMovie[] movies = searchResult.getMovies();
        resolveMoviesPanel = new RTResolveMoviesPanel(movies);
    }


}

