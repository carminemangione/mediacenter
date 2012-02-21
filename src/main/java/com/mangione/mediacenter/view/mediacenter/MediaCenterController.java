package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.model.VideoDirectories;
import com.mangione.mediacenter.model.mplayerx.KillMplayerX;
import com.mangione.mediacenter.model.mplayerx.LaunchMplayerXAndWaitForTerminate;
import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.managevideodirectories.ManageVideoDirectoriesController;
import com.mangione.mediacenter.view.moviebrowser.MovieBrowserController;
import com.mangione.mediacenter.view.rottentomatoes.RTDetailsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * User: carminemangione
 * Date: Feb 22, 2009
 * Time: 1:58:54 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class MediaCenterController implements MediaCenterControllerInterface {
    private final MovieBrowserController movieBrowserController;
    private final JPanel panelWithBorder;

    private MediaCenterView mediaCenterView;
    private RTDetailsController imdbDetailsController;
    private volatile boolean handlingscroll = false;

    public static void main(String[] args) throws Exception {
        VideoFiles videoFiles = loadVideoFiles();
        new MediaCenterController(videoFiles);
    }

    public MediaCenterController(VideoFiles videoFiles) throws Exception {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MediaCenter");
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        Dimension maximumWindowSize = new Dimension(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight());

        movieBrowserController = new MovieBrowserController(this, maximumWindowSize, videoFiles);
        panelWithBorder = movieBrowserController.getMovieBrowser();

        mediaCenterView = new MediaCenterView(panelWithBorder, graphicsDevice);
        mediaCenterView.addKeyListener(new ScrollKeyListener());

        mediaCenterView.addMouseListener(new PopupMenuMouseListener());
        mediaCenterView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    @Override
    public void playerFinishedPlaying() {
        mediaCenterView.windowToBack(false);
    }

    @Override
    public void videoSelectionFinished() {
        Cursor oldCursor = panelWithBorder.getCursor();
        panelWithBorder.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        movieBrowserController.setVideoFiles(loadVideoFiles());
        movieBrowserController.setDim(false);
        mediaCenterView.windowToBack(true);
        panelWithBorder.setCursor(oldCursor);
    }

    @Override
    public void showMovieDetails(VideoFile selectedVideoFile, int xPos, int yPos) {
        try {
//            imdbDetailsController = new ImdbDetailsController(selectedVideoFile, panelWithBorder, xPos, yPos);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void killMovieDetails() {
//        imdbDetailsController.killDetails();

    }

    private static VideoFiles loadVideoFiles() {
        String[] movieDirs = getVideoFileDirectories();
        return new VideoFiles(movieDirs);
    }

    private static String[] getVideoFileDirectories() {
        return VideoDirectories.getInstance().getVideoDirectories();
    }

    private class PopupMenuMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent mouseEvent) {
            showPopupIfTrigger(mouseEvent);
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            showPopupIfTrigger(mouseEvent);
        }

        private void showPopupIfTrigger(final MouseEvent mouseEvent) {
            if (mouseEvent.isPopupTrigger()) {
                movieBrowserController.setDim(true);
                // mediaCenterView.windowToBack(true);
                new ManageVideoDirectoriesController(mediaCenterView, mouseEvent.getPoint(), MediaCenterController.this);

            }
        }
    }

    private class ScrollKeyListener implements KeyListener {

        private boolean lastEventWasKeyPressed = false;
        private long lastAutoKeyTimeMillis = 0;



        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (imdbDetailsController != null) {
//                imdbDetailsController.killDetails();
                imdbDetailsController = null;
            }
            char keyPressed = keyEvent.getKeyChar();
            if (Character.isDigit(keyPressed) || Character.isLetter(keyPressed)) {
                movieBrowserController.zoomToLetter(keyPressed);
            } else {
                if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                    new KillMplayerX();
                    VideoFile videoFile = movieBrowserController.getCurrentVideoFile();
                    mediaCenterView.windowToBack(true);
                    new LaunchMplayerXAndWaitForTerminate(videoFile);
                    mediaCenterView.windowToBack(false);
                } else {
                    scrollOneLineOrHandleContinual(keyEvent);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            lastEventWasKeyPressed = false;
        }

        private void scrollOneLineOrHandleContinual(KeyEvent keyEvent) {
            synchronized (this) {
                try {
                    if (!handlingscroll) {
                        handlingscroll = true;
                        if (!lastEventWasKeyPressed || System.currentTimeMillis() -
                                lastAutoKeyTimeMillis > 50) {
                            movieBrowserController.arrowPressed(keyEvent, lastEventWasKeyPressed);
                            lastAutoKeyTimeMillis = System.currentTimeMillis();
                            handlingscroll = false;
                        }
                        handlingscroll = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                lastEventWasKeyPressed = true;
            }
        }

    }
}