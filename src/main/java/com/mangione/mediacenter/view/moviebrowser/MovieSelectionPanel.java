package com.mangione.mediacenter.view.moviebrowser;

import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.SharedConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MovieSelectionPanel extends JPanel {
    private static final Font FONT = SharedConstants.BASE_FONT.deriveFont(Font.BOLD, 20);
    private final JLabel movieTitle;
    private final MovieBrowserPanel movieBrowserPanel;

    public MovieSelectionPanel(VideoFiles moviesFiles) throws Exception {
        setOpaque(false);
        setLayout(new BorderLayout());

        movieBrowserPanel = new MovieBrowserPanel(moviesFiles);
        movieTitle = createAndInitializeCurrentMovie(movieBrowserPanel.getCurrentVideoFile().getVideoName());

        add(movieTitle, BorderLayout.NORTH);
        add(movieBrowserPanel, BorderLayout.CENTER);
    }

    private JLabel createAndInitializeCurrentMovie(String currentMovie) {
        JLabel movieTitle;
        movieTitle = new JLabel(currentMovie);
        movieTitle.setFont(FONT);
        movieTitle.setForeground(SharedConstants.LIGHT_TEXT_COLOR);
        movieTitle.setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        movieTitle.setHorizontalAlignment(SwingConstants.CENTER);
        movieTitle.setVerticalAlignment(SwingConstants.CENTER);
        return movieTitle;
    }

    public void movieTitleChanged(String newMovieTitle) {
        SwingUtilities.invokeLater(() -> {
            movieTitle.setText(newMovieTitle);
            repaint();
        });
    }

    public void setVideoFiles(VideoFiles videoFiles) {
        movieBrowserPanel.setVideoFiles(videoFiles);
    }

    public void setDim(boolean b) {
        movieBrowserPanel.setDim(b);
    }

    public void zoomToLetter(char keyPressed) {
        movieBrowserPanel.zoomToLetter(keyPressed);
        movieTitleChanged(movieBrowserPanel.getCurrentVideoFile().getVideoName());
    }

    public VideoFile getCurrentVideoFile() {
        return movieBrowserPanel.getCurrentVideoFile();
    }

    public void arrowPressed(KeyEvent keyEvent, boolean lastEventWasKeyPressed) throws Exception {
        movieBrowserPanel.arrowPressed(keyEvent, lastEventWasKeyPressed);
        movieTitleChanged(movieBrowserPanel.getCurrentVideoFile().getVideoName());
    }

}
