package com.mangione.mediacenter.view.moviebrowser;

import com.mangione.mediacenter.model.mplayerx.KillMplayerX;
import com.mangione.mediacenter.model.mplayerx.LaunchMplayerXAndWaitForTerminate;
import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.mediacenter.MediaCenterControllerInterface;
import com.mangione.mediacenter.view.mediacenter.ScrollKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MovieSelectionController {

    private final MovieSelectionPanel movieSelectionPanel;
    private final MediaCenterControllerInterface mediaCenterController;
    private final ScrollKeyListener panelKeyListener;
    private VideoFiles videoFiles;

    public MovieSelectionController(VideoFiles videoFiles,
            MediaCenterControllerInterface mediaCenterController) throws Exception {
        this.mediaCenterController = mediaCenterController;
        this.movieSelectionPanel = new MovieSelectionPanel(videoFiles);
        panelKeyListener = new ScrollKeyListener(this);
        movieSelectionPanel.addKeyListener(panelKeyListener);

    }

    public JPanel getMovieSelectionPanel() {
        return movieSelectionPanel;
    }

    public void setVideoFiles(VideoFiles videoFiles) {
        this.videoFiles = videoFiles;
        movieSelectionPanel.setVideoFiles(this.videoFiles);
        if (videoFiles.getNumberOfVideoFiles() > 0) {
            mediaCenterController.videoSelectionChanged(movieSelectionPanel.getCurrentVideoFile());
            mediaCenterController.startPopupTimer();
        }
    }


    public void zoomToLetter(char keyPressed) {
        if (videoFiles != null && videoFiles.getNumberOfVideoFiles() > 0) {
            movieSelectionPanel.zoomToLetter(keyPressed);
            mediaCenterController.videoSelectionChanged(movieSelectionPanel.getCurrentVideoFile());
        }
    }


    public void arrowPressed(KeyEvent keyEvent, boolean lastEventWasKeyPressed) throws Exception {
        movieSelectionPanel.arrowPressed(keyEvent, lastEventWasKeyPressed);
        mediaCenterController.videoSelectionChanged(movieSelectionPanel.getCurrentVideoFile());
    }

    public void arrowPressedFromDismissedPopup(KeyEvent keyEvent ) throws Exception {
        panelKeyListener.setSelectionChanged(keyEvent);
    }


    public void killPopupWindow() {
        panelKeyListener.popupDismissed();
    }

    public void spacePressed() {
        new KillMplayerX();
        VideoFile videoFile = movieSelectionPanel.getCurrentVideoFile();
        new LaunchMplayerXAndWaitForTerminate(videoFile);

    }

    public VideoFile getCurrentlySelectedVideo() {
        return movieSelectionPanel.getCurrentVideoFile();
    }

    public void popupMovieDetails() {
        mediaCenterController.popupMovieDetails();

    }

    public void startMovieDetailsPopupTimer() {
        panelKeyListener.startPopupTimer();
    }

    public Point getTopCenterForMovieDetails() {
         return movieSelectionPanel.getLocationForMovieDetails();
     }

    public int getHeightOfCurrentView() {
        return movieSelectionPanel.getHeight();
    }
}
