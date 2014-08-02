package com.mangione.mediacenter.view.rottentomatoes;

import com.mangione.mediacenter.view.SharedConstants;
import com.mangione.mediacenter.view.rottentomatoes.moviedetails.MovieDetailsController;

import javax.swing.*;
import java.awt.*;

public class RTMainController {
    private static final Dimension PREFERRED_SIZE = new Dimension(300, 316);

    private final RottenTomatoesControllerInterface currentController;

    public static void main(String[] args) throws Exception {
        RTMainController mainController = new RTMainController();
        JFrame frame = new JFrame();
        frame.setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        frame.setContentPane(mainController.currentController.getPanel());
        frame.validate();
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public RTMainController() throws Exception {
        String movieDetailsLink = "http://api.rottentomatoes.com/api/public/v1.0/movies/17414.json";
        currentController = new MovieDetailsController(movieDetailsLink);
        final JPanel resolveMoviesPanel = currentController.getPanel();
        resolveMoviesPanel.setPreferredSize(PREFERRED_SIZE);
    }
}
