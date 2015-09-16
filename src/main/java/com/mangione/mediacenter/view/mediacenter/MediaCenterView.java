package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.view.SharedConstants;
import com.mangione.mediacenter.view.managevideodirectories.ManageVideoDirectoriesController;
import com.mangione.mediacenter.view.moviebrowser.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


public class MediaCenterView extends JFrame {
    private final MediaCenterController mediaCenterController;
    private JDialog popup;
    private KeyEventDispatcher keyEventDispatcher;

    public MediaCenterView(MediaCenterController mediaCenterController, JComponent mediaImageGrid) throws HeadlessException {
        this.mediaCenterController = mediaCenterController;
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        setLayout(new BorderLayout(20, 0));

        add(mediaImageGrid, BorderLayout.CENTER);
        getContentPane().setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        setPreferredSize(new Dimension(1500, 900));
        mediaImageGrid.setFocusable(true);
        mediaImageGrid.requestFocus();

        validate();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void popupMovieDetails(final Component movieDetails, Rectangle boundingBoxOfCurrentSelection) {

        popup = new JDialog(this, true);
        popup.setLayout(new BorderLayout());
        popup.add(movieDetails, BorderLayout.CENTER);
        popup.setUndecorated(true);
        popup.invalidate();
        keyEventDispatcher = e -> {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                case KeyEvent.VK_UP:
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_RIGHT:
                    removePopup();
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);
                    keyEventDispatcher = null;
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        return true;
                    }
                    mediaCenterController.handlePassedOnArrowPressed(e);
                    return true;

                default:
                    return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(keyEventDispatcher);
        popup.pack();
        final Point topLeft = boundingBoxOfCurrentSelection.getTopLeft();
        Point locationForPopup = new Point(topLeft.x, topLeft.y - popup.getPreferredSize().height / 2);
        popup.setLocation(locationForPopup);
        popup.setVisible(true);
    }

    public void removePopup() {
        if (popup != null) {
            popup.dispose();
            popup = null;
        }
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
