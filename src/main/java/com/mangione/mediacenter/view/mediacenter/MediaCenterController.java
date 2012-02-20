package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.model.VideoDirectories;
import com.mangione.mediacenter.model.mplayerx.KillMplayerX;
import com.mangione.mediacenter.model.mplayerx.LaunchMplayerXAndWaitForTerminate;
import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.imdbDetails.ImdbDetailsController;
import com.mangione.mediacenter.view.managevideodirectories.ManageVideoDirectoriesController;
import com.mangione.mediacenter.view.movieimagegrid.MovieImageGrid;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * User: carminemangione
 * Date: Feb 22, 2009
 * Time: 1:58:54 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class MediaCenterController implements MediaCenterControllerInterface {
    private MovieImageGrid panelWithBorder;

    private MediaCenterView mediaCenterView;
    private ImdbDetailsController imdbDetailsController;
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

        panelWithBorder = new MovieImageGrid(maximumWindowSize, videoFiles);

        mediaCenterView = new MediaCenterView(panelWithBorder, graphicsDevice);
        mediaCenterView.addKeyListener(new ScrollKeyListener(panelWithBorder));

        mediaCenterView.addMouseListener(new PopupMenuMouseListener(panelWithBorder));
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
        panelWithBorder.setVideoFiles(loadVideoFiles());
        panelWithBorder.setDim(false);
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
        private final MovieImageGrid panelWithBorder;

        public PopupMenuMouseListener(MovieImageGrid panelWithBorder) {
            this.panelWithBorder = panelWithBorder;
        }

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
                panelWithBorder.setDim(true);
                // mediaCenterView.windowToBack(true);
                new ManageVideoDirectoriesController(mediaCenterView, mouseEvent.getPoint(), MediaCenterController.this);

            }
        }
    }

    private class ScrollKeyListener implements KeyListener {
        private final MovieImageGrid panelWithBorder;
        private boolean lastEventWasKeyPressed = false;
        private long lastAutoKeyTimeMillis = 0;

        public ScrollKeyListener(MovieImageGrid panelWithBorder) {
            this.panelWithBorder = panelWithBorder;
        }

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
                panelWithBorder.zoomToLetter(keyPressed);
            } else {
                if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                    new KillMplayerX();
                    VideoFile videoFile = panelWithBorder.getCurrentVideoFile();
                    new LaunchMplayerXAndWaitForTerminate(videoFile);
//                    launchCommand(videoFile.getLaunchMovieCommand());

//                    mediaCenterView.windowToBack(true);
//                    new MediaPlayer(command, videoFile.getApplicationName(), MediaCenterController.this);
                } else {
                    scrollOneLineOrHandleContinual(keyEvent);
                }
            }
        }

        private Thread launchCommand(final String command) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Process process = Runtime.getRuntime().exec(command);
                        InputStream istrm = process.getInputStream();
                        InputStreamReader istrmrdr = new InputStreamReader(istrm);
                        BufferedReader buffrdr = new BufferedReader(istrmrdr);

                        String data;
                        while ((data = buffrdr.readLine()) != null) {
                            System.out.println(data);
                        }
//                        process.waitFor();
//                        mediaCenterControllerInterface.playerFinishedPlaying();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
            return thread;
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
                            panelWithBorder.arrowPressed(keyEvent, lastEventWasKeyPressed);
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