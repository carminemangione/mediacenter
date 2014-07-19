package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.view.components.ImagePanel;

import javax.swing.*;
import java.awt.*;

public class RTResolveMovieListCellRender extends JPanel implements ListCellRenderer<DisplayableRTMovie>{
    private static final Font SELECTION_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);

    @Override
    public Component getListCellRendererComponent(JList<? extends DisplayableRTMovie> list, DisplayableRTMovie movie, int index, boolean isSelected, boolean cellHasFocus) {
        setLayout(new BorderLayout());
        setOpaque(false);

        ImagePanel imagePanel = new ImagePanel(movie.getPosterIcon(), 50, 100);
        add(imagePanel, BorderLayout.WEST);
        final String text = movie.getTitle() + " (" + movie.getYear() + ")";
        JLabel descriptionPanel = new JLabel(text);
        descriptionPanel.setFont(SELECTION_FONT);
        add(descriptionPanel, BorderLayout.CENTER);
        return this;
    }
}

