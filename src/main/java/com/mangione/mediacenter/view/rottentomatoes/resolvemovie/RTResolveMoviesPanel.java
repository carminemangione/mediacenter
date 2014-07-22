package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;


import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;

import javax.swing.*;
import java.net.MalformedURLException;
import java.util.Arrays;

public class RTResolveMoviesPanel extends JPanel {

    public RTResolveMoviesPanel(RTMovie[] movies) throws Exception {

        JComponent moviesPanel = createMoviesPanel(movies);

        JScrollPane scrollPane = new JScrollPane(moviesPanel);
        scrollPane.setViewportView(moviesPanel);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane);
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
        rtMovieJList.setVisibleRowCount(3);
        return rtMovieJList;
    }



}



