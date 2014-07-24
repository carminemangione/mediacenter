package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.model.mplayerx.KillMplayerX;
import com.mangione.mediacenter.model.mplayerx.LaunchMplayerXAndWaitForTerminate;
import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.view.moviebrowser.MovieSelectionController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

public class ScrollKeyListener implements KeyListener {

    private final MovieSelectionController movieSelectionController;
    private final MediaCenterView mediaCenterView;

    private volatile boolean handlingScroll;
    private boolean lastEventWasKeyPressed = false;
    private long lastAutoKeyTimeMillis = 0;

    public ScrollKeyListener(MovieSelectionController movieSelectionController, MediaCenterView mediaCenterView) {
        this.movieSelectionController = movieSelectionController;
        this.mediaCenterView = mediaCenterView;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        char keyPressed = keyEvent.getKeyChar();
        if (Character.isDigit(keyPressed) || Character.isLetter(keyPressed)) {
            movieSelectionController.zoomToLetter(keyPressed);
        } else {
            if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                new KillMplayerX();
                VideoFile videoFile = movieSelectionController.getCurrentVideoFile();
                new LaunchMplayerXAndWaitForTerminate(videoFile);
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
                mediaCenterView.dispatchEvent(new WindowEvent(mediaCenterView, WindowEvent.WINDOW_CLOSING));
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
                if (!handlingScroll) {
                    handlingScroll = true;
                    if (!lastEventWasKeyPressed || System.currentTimeMillis() -
                            lastAutoKeyTimeMillis > 50) {
                        movieSelectionController.arrowPressed(keyEvent, lastEventWasKeyPressed);
                        lastAutoKeyTimeMillis = System.currentTimeMillis();
                        handlingScroll = false;
                    }
                    handlingScroll = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            lastEventWasKeyPressed = true;
        }
    }

}
