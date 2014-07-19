package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;


import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.util.Arrays;

/**
 * User: carminemangione
 * Date: Dec 28, 2009
 * Time: 9:33:03 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class RTResolveMoviesPanel extends JPanel {

    public RTResolveMoviesPanel(RTMovie[] movies) throws Exception {

        JComponent moviesPanel = createMoviesPanel(movies);

//        JScrollPane scrollPane = new JScrollPane(moviesPanel);
//        scrollPane.setPreferredSize(new Dimension(moviesPanel.getPreferredSize().width, 300));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(moviesPanel);
        setVisible(true);
        validate();
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
    }

    private JList<DisplayableRTMovie> createMoviesPanel(RTMovie[] movies) throws MalformedURLException {

        final ImmutableList<DisplayableRTMovie> displayableRTMovies = FluentIterable
                .from(Arrays.asList(movies))
                .transform(rtMovie -> {
                    try {
                        return new DisplayableRTMovie(rtMovie);
                    } catch (MalformedURLException e) {
                        return null;
                    }
                }).toList();

        DisplayableRTMovie[] array = displayableRTMovies.toArray(new DisplayableRTMovie[displayableRTMovies.size()]);
        final JList<DisplayableRTMovie> rtMovieJList = new JList<>(array);
        rtMovieJList.setCellRenderer(new RTResolveMovieListCellRender());
        return rtMovieJList;
    }



}



