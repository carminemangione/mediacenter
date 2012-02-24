package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.model.VideoDirectories;
import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.managevideodirectories.ManageVideoDirectoriesController;
import com.mangione.mediacenter.view.moviebrowser.MovieBrowserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: carminemangione
 * Date: Feb 22, 2009
 * Time: 1:58:54 PM
 */
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
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        Dimension maximumWindowSize = new Dimension(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight());

        movieBrowserController = new MovieBrowserController(this, maximumWindowSize, videoFiles);
        panelWithBorder = movieBrowserController.getMovieBrowser();

        mediaCenterView = new MediaCenterView(panelWithBorder, graphicsDevice);
        mediaCenterView.addKeyListener(new ScrollKeyListener(movieBrowserController, mediaCenterView));

        mediaCenterView.addMouseListener(new PopupMenuMouseListener());
        mediaCenterView.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    @Override
    public void playerFinishedPlaying() {
        mediaCenterView.windowToBack(false);
    }

    @Override
    public void videoSelectionFinished() {
        Cursor oldCursor = panelWithBorder.getCursor();
        panelWithBorder.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        movieBrowserController.setVideoFiles(loadVideoFiles());
        movieBrowserController.setDim(false);
        mediaCenterView.windowToBack(true);
        panelWithBorder.setCursor(oldCursor);
    }

    @Override
    public void showMovieDetails(VideoFile selectedVideoFile, int xPos, int yPos) {

    }

    @Override
    public void killMovieDetails() {

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