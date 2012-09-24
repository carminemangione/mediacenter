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
        setLayout(new BorderLayout());
        setOpaque(false);
        ImagePanel imagePanel = new ImagePanel(posterUrl, 50, 100);
        add(imagePanel, BorderLayout.WEST);
        Font font = getFont().deriveFont(Font.TRUETYPE_FONT, 9);
        final String text = movie.getTitle() + " (" + movie.getYear() + ")";
        JLabel descriptionPanel = new JLabel(text);
        descriptionPanel.setFont(font);
        add(descriptionPanel, BorderLayout.CENTER);
    }
}

