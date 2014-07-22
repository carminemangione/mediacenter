package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.model.VideoDirectories;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.managevideodirectories.ManageVideoDirectoriesController;
import com.mangione.mediacenter.view.moviebrowser.MovieBrowserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MediaCenterController implements MediaCenterControllerInterface {
    private final MovieBrowserController movieBrowserController;
    private final JPanel panelWithBorder;

    private MediaCenterView mediaCenterView;

    public static void main(String[] args) throws Exception {
        VideoFiles videoFiles = loadVideoFiles();
        new MediaCenterController(videoFiles);
    }

    public MediaCenterController(VideoFiles videoFiles) throws Exception {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MediaCenter");

        movieBrowserController = new MovieBrowserController(videoFiles);
        panelWithBorder = movieBrowserController.getMovieBrowser();

        mediaCenterView = new MediaCenterView(panelWithBorder);
        mediaCenterView.addKeyListener(new ScrollKeyListener(movieBrowserController, mediaCenterView));

        mediaCenterView.addMouseListener(new PopupMenuMouseListener());

    }

    @Override
    public void videoSelectionFinished() {
        Cursor oldCursor = panelWithBorder.getCursor();
        panelWithBorder.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        movieBrowserController.setVideoFiles(loadVideoFiles());
        movieBrowserController.setDim(false);
        panelWithBorder.setCursor(oldCursor);
    }

    private static VideoFiles loadVideoFiles() {
        String[] movieDirs = getVideoFileDirectories();
        return new VideoFiles(movieDirs);
    }

    private static String[] getVideoFileDirectories() {
        return VideoDirectories.getInstance().getVideoDirectories();
    }

    private class PopupMenuMouseListener extends MouseAdapter {

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
                movieBrowserController.setDim(true);
                new ManageVideoDirectoriesController(mediaCenterView, mouseEvent.getPoint(), MediaCenterController.this);

            }
        }
    }

}