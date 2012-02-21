package com.mangione.mediacenter.view.rottentomatoes;

import com.mangione.mediacenter.model.rottentomatoes.RTMovie;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class RTDetailPanel extends JPanel {

    public RTDetailPanel(RTMovie movie) throws MalformedURLException {
        final URL posterUrl = new URL(movie.getPosters().getThumbnail());
        ImagePanel imagePanel = new ImagePanel(posterUrl);
        add(imagePanel);

        JPanel descriptionPanel = new JPanel(new FlowLayout(10));
        descriptionPanel.add(new JLabel(movie.getTitle()));
        descriptionPanel.add(new JLabel(movie.getYear()));
        add(descriptionPanel);
    }
}

