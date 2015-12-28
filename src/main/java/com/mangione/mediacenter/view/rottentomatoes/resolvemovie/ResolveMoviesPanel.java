package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;


import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.util.Arrays;

public class ResolveMoviesPanel extends JPanel {

    private final RTMovie[] movies;
    private final ResolveMoviesController resolveMoviesController;

    public ResolveMoviesPanel(RTMovie[] movies, ResolveMoviesController resolveMoviesController) throws Exception {
        this.movies = movies;
        this.resolveMoviesController = resolveMoviesController;
        setOpaque(false);
        setLayout(new BorderLayout());

        JList moviesPanel = createMoviesPanel();
        moviesPanel.setSelectedIndex(0);

        JScrollPane scrollPane = new JScrollPane(moviesPanel);
        scrollPane.setOpaque(false);
        moviesPanel.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportView(moviesPanel);
        add(scrollPane, BorderLayout.CENTER);

    }

    private JList<DisplayableMovie> createMoviesPanel() throws MalformedURLException {

        final ImmutableList<DisplayableMovie> displayableRTMovies = FluentIterable
                .from(Arrays.asList(movies))
                .transform(DisplayableMovie::new).toList();

        DisplayableMovie[] array = displayableRTMovies.toArray(new DisplayableMovie[displayableRTMovies.size()]);
        final JList<DisplayableMovie> rtMovieJList = new JList<>(array);
        rtMovieJList.setCellRenderer(new ResolveMovieListCellRender());
        rtMovieJList.setVisibleRowCount(3);

        rtMovieJList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                resolveMoviesController.resolveMovieSelected(movies[list.getSelectedIndex()]);

            }
        });

        return rtMovieJList;
    }



}



