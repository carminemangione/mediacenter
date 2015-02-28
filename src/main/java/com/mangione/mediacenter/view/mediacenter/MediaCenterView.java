package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.view.SharedConstants;
import com.mangione.mediacenter.view.managevideodirectories.ManageVideoDirectoriesController;

import javax.swing.*;
import java.awt.*;


public class MediaCenterView extends JFrame {
    private final MediaCenterController mediaCenterController;
    private Component currentEastComponent;

    public MediaCenterView(MediaCenterController mediaCenterController, JComponent mediaImageGrid) throws HeadlessException {
        this.mediaCenterController = mediaCenterController;
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

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

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        final JMenu fileMenu = new JMenu("File");
        final JMenuItem videoDirectories = new JMenuItem("Manage Video Directories...");
        videoDirectories.addActionListener(actionEvent -> new ManageVideoDirectoriesController(mediaCenterController).launchManageDirectoriesWindow(this));

        fileMenu.add(videoDirectories);

        menuBar.add(fileMenu);
        return menuBar;
    }

}
