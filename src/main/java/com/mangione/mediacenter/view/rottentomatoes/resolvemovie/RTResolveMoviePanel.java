package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.model.rottentomatoes.RTMovie;
import com.mangione.mediacenter.view.panels.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class RTResolveMoviePanel extends JPanel {

    public RTResolveMoviePanel(RTMovie movie) throws MalformedURLException {
        final URL posterUrl = new URL(movie.getPosters().getThumbnail());
        ImagePanel imagePanel = new ImagePanel(posterUrl, 50, 100);
        add(imagePanel);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.add(new JLabel(movie.getTitle()), BorderLayout.CENTER);
        descriptionPanel.add(new JLabel(movie.getYear()), BorderLayout.EAST);
        add(descriptionPanel);
        setBackground(Color.lightGray);
        setAlignmentX(FlowLayout.LEFT);
    }
}

