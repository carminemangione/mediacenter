package com.mangione.mediacenter.view.moviebrowser;

import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.mediacenter.MediaCenterControllerInterface;
import com.mangione.mediacenter.view.mediacenter.ScrollKeyListener;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MovieSelectionController {

    private final MovieSelectionPanel movieSelectionPanel;
    private final MediaCenterControllerInterface mediaCenterController;

    public MovieSelectionController(VideoFiles movieDirs,
            MediaCenterControllerInterface mediaCenterController) throws Exception {
        this.mediaCenterController = mediaCenterController;
        this.movieSelectionPanel = new MovieSelectionPanel(movieDirs);
        movieSelectionPanel.addKeyListener(new ScrollKeyListener(this));


    }

    public JPanel getMovieSelectionPanel() {
        return movieSelectionPanel;
    }

    public void setVideoFiles(VideoFiles videoFiles) {
        movieSelectionPanel.setVideoFiles(videoFiles);
        mediaCenterController.videoSelectionChanged(movieSelectionPanel.getCurrentVideoFile());
    }

    public void setDim(boolean b) {
        movieSelectionPanel.setDim(b);
    }

    public void zoomToLetter(char keyPressed) {
        movieSelectionPanel.zoomToLetter(keyPressed);
        mediaCenterController.videoSelectionChanged(movieSelectionPanel.getCurrentVideoFile());
    }


    public VideoFile getCurrentVideoFile() {
        return movieSelectionPanel.getCurrentVideoFile();
    }

    public void arrowPressed(KeyEvent keyEvent, boolean lastEventWasKeyPressed) throws Exception {
        movieSelectionPanel.arrowPressed(keyEvent, lastEventWasKeyPressed);
        mediaCenterController.videoSelectionChanged(movieSelectionPanel.getCurrentVideoFile());
    }

    public void escPressed() {
        mediaCenterController.exitRequested();
    }
}
