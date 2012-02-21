package com.mangione.mediacenter.view.moviebrowser;

import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.mediacenter.MediaCenterControllerInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MovieBrowserController {

    private final MediaCenterControllerInterface mediaCenterController;
    private final MovieBrowser movieBrowser;

    public MovieBrowserController(MediaCenterControllerInterface mediaCenterController, Dimension screenSize, VideoFiles movieDirs) throws Exception {
        this.mediaCenterController = mediaCenterController;
        this.movieBrowser = new MovieBrowser(screenSize, movieDirs);
    }

    public JPanel getMovieBrowser() {
        return movieBrowser;
    }

    public void setVideoFiles(VideoFiles videoFiles) {
        movieBrowser.setVideoFiles(videoFiles);
    }

    public void setDim(boolean b) {
        movieBrowser.setDim(b);
    }

    public void zoomToLetter(char keyPressed) {
        movieBrowser.zoomToLetter(keyPressed);
    }


    public VideoFile getCurrentVideoFile() {
        return movieBrowser.getCurrentVideoFile();
    }

    public void arrowPressed(KeyEvent keyEvent, boolean lastEventWasKeyPressed) throws Exception {
        movieBrowser.arrowPressed(keyEvent, lastEventWasKeyPressed);
    }
}
