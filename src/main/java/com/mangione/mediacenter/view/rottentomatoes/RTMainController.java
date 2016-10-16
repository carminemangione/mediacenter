package com.mangione.mediacenter.view.rottentomatoes;

import com.mangione.mediacenter.model.rottentomatoes.database.ArchivedMovies;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.DetailsAndSynopsis;
import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;
import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.view.SharedConstants;
import com.mangione.mediacenter.view.rottentomatoes.moviedetails.MovieDetailsController;
import com.mangione.mediacenter.view.rottentomatoes.resolvemovie.MovieResolvedListener;
import com.mangione.mediacenter.view.rottentomatoes.resolvemovie.ResolveMoviesController;
import com.mangione.mediacenter.view.rottentomatoes.rottentomatoessearch.ResearchController;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class RTMainController implements MovieResolvedListener {
    private static final Dimension PREFERRED_SIZE = new Dimension(400, 516);
    private static final LoadingController LOADING_CONTROLLER = new LoadingController();

    private RottenTomatoesControllerInterface currentController;
    private String currentMovieInSearch;
    private JPanel mainPanel;
    private volatile Thread movieLoadingThread;
    private final ResearchController researchController;
    private VideoFile videoFile;

    public RTMainController() throws Exception {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(true);
        mainPanel.setPreferredSize(PREFERRED_SIZE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(SharedConstants.POPUP_BACKGROUND_COLOR);
        researchController = new ResearchController(this);
    }

    public synchronized void loadMovie(final VideoFile videoFile) throws SQLException {
        this.videoFile = videoFile;
        currentMovieInSearch = this.videoFile.getIdentifyingString();
        final DetailsAndSynopsis detailsAndSynopsis = ArchivedMovies.getInstance().getMovie(currentMovieInSearch);
        if (detailsAndSynopsis == null) {
            final String videoName = videoFile.getSearchVideoName();
            searchRottenTomatoesAndFlipToResolvePanel(videoName);
        } else {
            try {
                currentController = new MovieDetailsController(detailsAndSynopsis);
                flipPanel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void reSearch(String returnText) {
        try {
            ArchivedMovies.getInstance().deleteMovie(videoFile.getIdentifyingString());
            currentController = LOADING_CONTROLLER;
            flipPanel();
            searchRottenTomatoesAndFlipToResolvePanel(returnText == null ? videoFile.getVideoName() : returnText);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void killPopup() {
        if (movieLoadingThread != null) {
            movieLoadingThread.interrupt();
            movieLoadingThread = null;
        }
    }

    @Override
    public void resolvedMovieSelected(RTMovie rtMovie) {
        final String movieLink = rtMovie.getLinks().getSelf();
        loadMovieAtLink(movieLink);
    }

    private synchronized void searchRottenTomatoesAndFlipToResolvePanel(final String videoName) {
        if (movieLoadingThread != null) {
            movieLoadingThread.interrupt();
        }
        currentController = LOADING_CONTROLLER;
        flipPanel();
        getSearchResultsAsynchronously(videoName);
    }

    private void getSearchResultsAsynchronously(final String videoName) {
        movieLoadingThread = new Thread() {
            public void run() {
                try {
                    currentController = new ResolveMoviesController(videoName, RTMainController.this);
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
            mainPanel.removeAll();
            mainPanel.add(flippedPanel, BorderLayout.CENTER);
            mainPanel.add(researchController.getResearchPanel(), BorderLayout.SOUTH);
            mainPanel.validate();
            mainPanel.repaint();
        });
    }
}
