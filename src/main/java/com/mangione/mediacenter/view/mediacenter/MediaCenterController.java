package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.model.VideoDirectories;
import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.SharedConstants;
import com.mangione.mediacenter.view.moviebrowser.MovieSelectionController;
import com.mangione.mediacenter.view.rottentomatoes.RTMainController;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MediaCenterController implements MediaCenterControllerInterface {
    private final MovieSelectionController movieSelectionController;
    private final MediaCenterView mediaCenterView;
    private final RTMainController rtMainController;

    public static void main(String[] args) throws Exception {
        VideoFiles videoFiles = loadVideoFiles();
        new MediaCenterController(videoFiles);
    }

    public MediaCenterController(VideoFiles videoFiles) throws Exception {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MediaCenter");

        movieSelectionController = new MovieSelectionController(videoFiles, this);
        rtMainController = new RTMainController();

        final JPanel movieSelectionPanel = movieSelectionController.getMovieSelectionPanel();
        mediaCenterView = new MediaCenterView(this, movieSelectionPanel);
        final JPanel blankPanel = new JPanel();
        blankPanel.setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        if (videoFiles.getNumberOfVideoFiles() > 0) {
            videoSelectionChanged(movieSelectionController.getCurrentlySelectedVideo());
        }
        mediaCenterView.setVisible(true);
        movieSelectionController.startMovieDetailsPopupTimer();
    }

    @Override
    public void videoSelectionChanged(VideoFile videoFile) {
        try {
            rtMainController.loadMovie(videoFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void videoSelectionFinished() {
        movieSelectionController.setVideoFiles(loadVideoFiles());
    }

    @Override
    public void escapePressed() {
        mediaCenterView.removePopup();
        movieSelectionController.killPopupWindow();
    }

    @Override
    public void popupMovieDetails() {
        mediaCenterView.popupMovieDetails(rtMainController.getMainPanel(),
                movieSelectionController.getTopCenterForMovieDetails(),
                movieSelectionController.getHeightOfCurrentView());
    }

    @Override
    public void startPopupTimer() {
        movieSelectionController.startMovieDetailsPopupTimer();
    }

    private static VideoFiles loadVideoFiles() {
        String[] movieDirs = getVideoFileDirectories();
        return new VideoFiles(movieDirs);
    }

    private static String[] getVideoFileDirectories() {
        return VideoDirectories.getInstance().getVideoDirectories();
    }

    public void handlePassedOnArrowPressed(KeyEvent keyCode) {
        try {
            movieSelectionController.arrowPressedFromDismissedPopup(keyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}