package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.model.mplayerx.KillMplayerX;
import com.mangione.mediacenter.model.mplayerx.LaunchMplayerXAndWaitForTerminate;
import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.view.moviebrowser.MovieBrowserController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ScrollKeyListener implements KeyListener {

    private final MovieBrowserController movieBrowserController;
    private final MediaCenterView mediaCenterView;

    private volatile boolean handlingscroll;
    private boolean lastEventWasKeyPressed = false;
    private long lastAutoKeyTimeMillis = 0;

    public ScrollKeyListener(MovieBrowserController movieBrowserController, MediaCenterView mediaCenterView) {
        this.movieBrowserController = movieBrowserController;
        this.mediaCenterView = mediaCenterView;
    }

    @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            char keyPressed = keyEvent.getKeyChar();
            if (Character.isDigit(keyPressed) || Character.isLetter(keyPressed)) {
                movieBrowserController.zoomToLetter(keyPressed);
            } else {
                if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                    new KillMplayerX();
                    VideoFile videoFile = movieBrowserController.getCurrentVideoFile();
                    mediaCenterView.windowToBack(true);
                    System.out.println(videoFile.getLaunchMovieCommand());
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
