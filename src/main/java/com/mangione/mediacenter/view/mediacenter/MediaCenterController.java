package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.model.VideoDirectories;
import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.SharedConstants;
import com.mangione.mediacenter.view.moviebrowser.MovieSelectionController;
import com.mangione.mediacenter.view.rottentomatoes.resolvemovie.ResolveMoviesController;

import javax.swing.*;
import java.awt.event.WindowEvent;

public class MediaCenterController implements MediaCenterControllerInterface {
    private final MovieSelectionController movieSelectionController;
    private final MediaCenterView mediaCenterView;

    public static void main(String[] args) throws Exception {
        VideoFiles videoFiles = loadVideoFiles();
        new MediaCenterController(videoFiles);
    }

    public MediaCenterController(VideoFiles videoFiles) throws Exception {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MediaCenter");

        movieSelectionController = new MovieSelectionController(videoFiles, this);


        final JPanel movieSelectionPanel = movieSelectionController.getMovieSelectionPanel();
        mediaCenterView = new MediaCenterView(movieSelectionPanel);
        final JPanel blankPanel = new JPanel();
        blankPanel.setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        videoSelectionChanged(movieSelectionController.getCurrentlySelectedVideo());
        mediaCenterView.setVisible(true);
    }

    @Override
    public void videoSelectionChanged(VideoFile videoFile)  {
        try {
            ResolveMoviesController resolveMoviesController = new ResolveMoviesController(movieSelectionController.getCurrentlySelectedVideo().getVideoName());
            mediaCenterView.setEastComponent(resolveMoviesController.getPanel());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void videoSelectionFinished() {
        movieSelectionController.setVideoFiles(loadVideoFiles());
        movieSelectionController.setDim(false);
    }

    @Override
    public void exitRequested() {
        mediaCenterView.dispatchEvent(new WindowEvent(mediaCenterView, WindowEvent.WINDOW_CLOSING));
    }

    private static VideoFiles loadVideoFiles() {
        String[] movieDirs = getVideoFileDirectories();
        return new VideoFiles(movieDirs);
    }

    private static String[] getVideoFileDirectories() {
        return VideoDirectories.getInstance().getVideoDirectories();
    }


}