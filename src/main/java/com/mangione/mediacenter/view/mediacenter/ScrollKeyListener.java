package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.view.moviebrowser.MovieSelectionController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ScrollKeyListener implements KeyListener {

    private final MovieSelectionController movieSelectionController;

    private volatile boolean handlingScroll;
    private boolean lastEventWasKeyPressed = false;
    private long lastAutoKeyTimeMillis = 0;
    private static int PAUSE_TIME_FOR_POPUP = 500;
    private boolean selectionChangedSinceLastPopup = false;

    private Timer popupTimer = new Timer(PAUSE_TIME_FOR_POPUP, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            System.out.println("ScrollKeyListener.actionPerformed: " + selectionChangedSinceLastPopup);
            if (selectionChangedSinceLastPopup) {
                movieSelectionController.popupMovieDetails();
            }
            popupTimer.stop();
        }
    });

    public ScrollKeyListener(MovieSelectionController movieSelectionController) {
        this.movieSelectionController = movieSelectionController;
    }

    public void startPopupTimer() {
        popupTimer.restart();

    }

    public void popupDismissed() {
        selectionChangedSinceLastPopup = false;
    }

    public void setSelectionChanged(KeyEvent e) {
        popupTimer.stop();
        handlingScroll = false;
        selectionChangedSinceLastPopup = true;
        lastEventWasKeyPressed = false;
        scrollOneLineOrHandleContinual(e);
        popupTimer.restart();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        popupTimer.stop();
        char keyPressed = keyEvent.getKeyChar();
        if (Character.isDigit(keyPressed) || Character.isLetter(keyPressed)) {

            movieSelectionController.zoomToLetter(keyPressed);
            selectionChangedSinceLastPopup = true;
        } else {
            final int keyCode = keyEvent.getKeyCode();
            if (keyCode == KeyEvent.VK_SPACE) {
                movieSelectionController.spacePressed();
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                movieSelectionController.killPopupWindow();
            } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN
                    || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT){
                selectionChangedSinceLastPopup = true;
                scrollOneLineOrHandleContinual(keyEvent);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        lastEventWasKeyPressed = false;
        popupTimer.restart();
    }

    private void scrollOneLineOrHandleContinual(KeyEvent keyEvent) {
        synchronized (this) {
            popupTimer.stop();
            try {
                if (!handlingScroll) {
                    handlingScroll = true;
                    if (!lastEventWasKeyPressed || System.currentTimeMillis() -
                            lastAutoKeyTimeMillis > 50) {
                        movieSelectionController.arrowPressed(keyEvent, lastEventWasKeyPressed);
                        lastAutoKeyTimeMillis = System.currentTimeMillis();
                        handlingScroll = false;
                        popupTimer.restart();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            lastEventWasKeyPressed = true;
        }
    }
}
