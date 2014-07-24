package com.mangione.mediacenter.view.moviebrowser;

import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MovieSelectionController {

    private final MovieSelectionPanel movieSelectionPanel;

    public MovieSelectionController(VideoFiles movieDirs) throws Exception {
        this.movieSelectionPanel = new MovieSelectionPanel(movieDirs);
    }

    public JPanel getMovieSelectionPanel() {
        return movieSelectionPanel;
    }

    public void setVideoFiles(VideoFiles videoFiles) {
        movieSelectionPanel.setVideoFiles(videoFiles);
    }

    public void setDim(boolean b) {
        movieSelectionPanel.setDim(b);
    }

    public void zoomToLetter(char keyPressed) {
        movieSelectionPanel.zoomToLetter(keyPressed);
    }


    public VideoFile getCurrentVideoFile() {
        return movieSelectionPanel.getCurrentVideoFile();
    }

    public void arrowPressed(KeyEvent keyEvent, boolean lastEventWasKeyPressed) throws Exception {
        movieSelectionPanel.arrowPressed(keyEvent, lastEventWasKeyPressed);
    }
}
