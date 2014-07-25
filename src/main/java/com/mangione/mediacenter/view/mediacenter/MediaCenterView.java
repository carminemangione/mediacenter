package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.view.SharedConstants;

import javax.swing.*;
import java.awt.*;


public class MediaCenterView extends JFrame {

    public MediaCenterView(JComponent mediaImageGrid, JPanel resolveMoviesPanel) throws HeadlessException {
        setLayout(new BorderLayout(20, 0));

        add(mediaImageGrid, BorderLayout.CENTER);
        add(resolveMoviesPanel, BorderLayout.EAST);
        getContentPane().setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        setPreferredSize(new Dimension(600, 500));
        mediaImageGrid.setFocusable(true);
        mediaImageGrid.requestFocus();

        validate();
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
