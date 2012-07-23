package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.model.rottentomatoes.RTMovie;
import com.mangione.mediacenter.model.rottentomatoes.RTSearchResult;
import com.mangione.mediacenter.model.rottentomatoes.RottenTomatoesSearch;

import javax.swing.*;
import java.awt.*;

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
        frame.setPreferredSize(new Dimension(1000, 1000));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        new RTResolveMoviesController(frame.getLayeredPane(), 50, 50, "under one roof");
    }

    public RTResolveMoviesController(JComponent parent, int locX, int locY, String movieName) throws Exception {
        final RTSearchResult searchResult = new RottenTomatoesSearch(movieName).getSearchResult();
        final RTMovie[] movies = searchResult.getMovies();
        resolveMoviesPanel = new RTResolveMoviesPanel(parent, locX, locY, movies);
    }

    public Component getPanel() {
        return resolveMoviesPanel;
    }
}

