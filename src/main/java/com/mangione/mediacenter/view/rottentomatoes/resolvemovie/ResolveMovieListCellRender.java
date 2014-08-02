package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.view.SharedConstants;
import com.mangione.mediacenter.view.components.AspectRatioPreservedImagePanel;

import javax.swing.*;
import java.awt.*;

public class ResolveMovieListCellRender implements ListCellRenderer<DisplayableMovie>{
    private static final Font SELECTION_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);

    @Override
    public Component getListCellRendererComponent(JList<? extends DisplayableMovie> list,
            DisplayableMovie movie, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setOpaque(true);
        panel.setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        Color borderColor = isSelected ? Color.PINK : Color.BLACK;

        panel.setBorder(BorderFactory.createLineBorder(borderColor, 2));
        AspectRatioPreservedImagePanel moviePosterPanel = getPosterImage(movie);

        panel.add(moviePosterPanel);
        final String text = movie.getTitle() + " (" + movie.getYear() + ")";
        JTextArea descriptionPanel = createDescriptionPanel(text);

        panel.add(descriptionPanel);
        return panel;
    }

    private AspectRatioPreservedImagePanel getPosterImage(DisplayableMovie movie) {
        AspectRatioPreservedImagePanel moviePosterPanel =
                new AspectRatioPreservedImagePanel(movie.getPosterImage());
        moviePosterPanel.setOpaque(false);
        moviePosterPanel.setPreferredSize(new Dimension(50, 100));
        return moviePosterPanel;
    }

    private JTextArea createDescriptionPanel(String text) {
        JTextArea descriptionPanel = new JTextArea(text);
        descriptionPanel.setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        descriptionPanel.setForeground(Color.cyan);
        descriptionPanel.setFont(SELECTION_FONT);
        descriptionPanel.setLineWrap(true);
        descriptionPanel.setWrapStyleWord(true);
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 0));
        return descriptionPanel;
    }
}

