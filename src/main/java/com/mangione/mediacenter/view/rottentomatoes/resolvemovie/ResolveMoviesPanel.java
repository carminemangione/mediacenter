package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;


import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;

public class ResolveMoviesPanel extends JPanel {

    public ResolveMoviesPanel(RTMovie[] movies) throws Exception {
        setOpaque(false);
        setLayout(new BorderLayout());

        JComponent moviesPanel = createMoviesPanel(movies);

        JScrollPane scrollPane = new JScrollPane(moviesPanel);
        scrollPane.setViewportView(moviesPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JList<DisplayableMovie> createMoviesPanel(RTMovie[] movies) throws MalformedURLException {

        final ImmutableList<DisplayableMovie> displayableRTMovies = FluentIterable
                .from(Arrays.asList(movies))
                .transform(rtMovie -> {
                    try {
                        return new DisplayableMovie(rtMovie);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }).toList();

        DisplayableMovie[] array = displayableRTMovies.toArray(new DisplayableMovie[displayableRTMovies.size()]);
        final JList<DisplayableMovie> rtMovieJList = new JList<>(array);
        rtMovieJList.setCellRenderer(new ResolveMovieListCellRender());
        rtMovieJList.setVisibleRowCount(3);
        return rtMovieJList;
    }



}



