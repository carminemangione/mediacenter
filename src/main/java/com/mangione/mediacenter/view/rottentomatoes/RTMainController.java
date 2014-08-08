package com.mangione.mediacenter.view.rottentomatoes;

import com.mangione.mediacenter.model.rottentomatoes.database.ArchivedMovies;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.DetailsAndSynopsis;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;
import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.view.SharedConstants;
import com.mangione.mediacenter.view.rottentomatoes.moviedetails.MovieDetailsController;
import com.mangione.mediacenter.view.rottentomatoes.resolvemovie.MovieResolvedListener;
import com.mangione.mediacenter.view.rottentomatoes.resolvemovie.ResolveMoviesController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;

public class RTMainController implements MovieResolvedListener {
    private static final Dimension PREFERRED_SIZE = new Dimension(300, 316);
    private static final LoadingController LOADING_CONTROLLER = new LoadingController();


    private RottenTomatoesControllerInterface currentController;
    private String currentMovieInSearch;
    private JPanel currentPanel = null;
    private volatile Thread movieLoadingThread;

    public static void main(String[] args) throws Exception {
        RTMainController mainController = new RTMainController();
        VideoFile videoFile = new VideoFile(new File("/Users/carmine/Movies/UNDER_ONE_ROOF/"), "Under.One.Roof.mp4");
        mainController.loadMovie(videoFile);

        JFrame frame = new JFrame();
        frame.setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        frame.setContentPane(mainController.getCurrentPanel());
        frame.validate();
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public RTMainController() throws Exception {
        currentPanel = new JPanel(new BorderLayout());
        currentPanel.setOpaque(true);
        currentPanel.setPreferredSize(PREFERRED_SIZE);
        currentPanel.setBorder(BorderFactory.createEmptyBorder(20,20, 20, 20));
        currentPanel.setBackground(new Color(84, 127, 165));


    }


    public synchronized void loadMovie(final VideoFile videoFile) throws SQLException {
        currentMovieInSearch = videoFile.getIdentifyingString();
        final DetailsAndSynopsis detailsAndSynopsis = ArchivedMovies.getInstance().getMovie(currentMovieInSearch);
        if (detailsAndSynopsis == null) {
            if (movieLoadingThread != null) {
                movieLoadingThread.interrupt();
            }
            currentController = LOADING_CONTROLLER;
            flipPanel();
            movieLoadingThread = new Thread() {
                public void run() {
                    try {
                        currentController = new ResolveMoviesController(videoFile.getVideoName(), RTMainController.this);
                        flipPanel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    synchronized (RTMainController.this) {
                        movieLoadingThread = null;
                    }
                }
            };
            movieLoadingThread.start();
        } else {
            try {
                currentController = new MovieDetailsController(detailsAndSynopsis);
                flipPanel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void resolvedMovieSelected(RTMovie rtMovie) {
        final String movieLink = rtMovie.getLinks().getSelf();
        loadMovieAtLink(movieLink);
    }

    private void loadMovieAtLink(final String movieLink) {
        try {
            if (movieLoadingThread != null) {
                movieLoadingThread.interrupt();
            }
            currentController = LOADING_CONTROLLER;
            flipPanel();
            movieLoadingThread = new Thread() {
                public void run() {
                    try {
                        currentController = new MovieDetailsController(currentMovieInSearch, movieLink);
                        flipPanel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    synchronized (RTMainController.this) {
                        movieLoadingThread = null;
                    }
                }
            };
            movieLoadingThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void flipPanel() {
        SwingUtilities.invokeLater(() -> {
            final JPanel flippedPanel = currentController.getPanel();
            flippedPanel.invalidate();
            currentPanel.removeAll();
            currentPanel.add(flippedPanel, BorderLayout.CENTER);
            currentPanel.validate();
            currentPanel.repaint();
        });
    }

    public JPanel getCurrentPanel() {
        return currentPanel;
    }
}
