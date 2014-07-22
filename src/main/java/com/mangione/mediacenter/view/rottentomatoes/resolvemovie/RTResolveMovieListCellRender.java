package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.view.components.AspectRatioPreservedImagePanel;

import javax.swing.*;
import java.awt.*;

public class RTResolveMovieListCellRender implements ListCellRenderer<DisplayableRTMovie>{
    private static final Font SELECTION_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);

    @Override
    public Component getListCellRendererComponent(JList<? extends DisplayableRTMovie> list,
            DisplayableRTMovie movie, int index, boolean isSelected, boolean cellHasFocus) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        AspectRatioPreservedImagePanel moviePosterPanel =
                new AspectRatioPreservedImagePanel(movie.getPosterIcon());
        moviePosterPanel.setOpaque(false);

        moviePosterPanel.setPreferredSize(new Dimension(50, 100));
        panel.add(moviePosterPanel, BorderLayout.WEST);
        final String text = movie.getTitle() + " (" + movie.getYear() + ")";
        JLabel descriptionPanel = new JLabel(text);
        descriptionPanel.setFont(SELECTION_FONT);
        panel.add(descriptionPanel, BorderLayout.CENTER);
        return panel;
    }
}

