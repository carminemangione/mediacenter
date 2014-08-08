package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.view.SharedConstants;

import javax.swing.*;
import java.awt.*;


public class MediaCenterView extends JFrame {
    private Component currentEastComponent;

    public MediaCenterView(JComponent mediaImageGrid) throws HeadlessException {
        setLayout(new BorderLayout(20, 0));

        add(mediaImageGrid, BorderLayout.CENTER);
        getContentPane().setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        setPreferredSize(new Dimension(900, 500));
        mediaImageGrid.setFocusable(true);
        mediaImageGrid.requestFocus();

        validate();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setEastComponent(final Component eastComponent) {
        SwingUtilities.invokeLater(() -> {
            if (currentEastComponent != null) {
                remove(currentEastComponent);
            }

            add(eastComponent, BorderLayout.EAST);
            currentEastComponent = eastComponent;
            eastComponent.invalidate();
            validate();
            repaint();
        });


    }

}
