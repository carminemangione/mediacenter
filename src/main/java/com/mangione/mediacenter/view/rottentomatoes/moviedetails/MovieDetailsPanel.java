package com.mangione.mediacenter.view.rottentomatoes.moviedetails;

import com.mangione.mediacenter.model.rottentomatoes.moviedetails.MovieDetails;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.Ratings;
import com.mangione.mediacenter.view.ImagePanel;
import com.mangione.mediacenter.view.components.AspectRatioPreservedImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MovieDetailsPanel extends JPanel {
    private static final Font SYNOPSIS_FONT = new Font("ITC Garamond", Font.ITALIC, 12);
    private static final Font STATS_FONT = new Font("ITC Garamond", Font.PLAIN, 14);

    private static final BufferedImage FRESH_IMAGE;
    private static final BufferedImage SPLASH_IMAGE;

    static {

        try {
            FRESH_IMAGE = ImageIO.read(MovieDetailsPanel.class.getClassLoader().getResourceAsStream("rottentomatoes/fresh.png"));
            SPLASH_IMAGE = ImageIO.read(MovieDetailsPanel.class.getClassLoader().getResourceAsStream("rottentomatoes/splash.png"));
        } catch (IOException e) {
            throw new RuntimeException("Could not load rotten tomato images",e);
        }
    }


    public MovieDetailsPanel(MovieDetails movieDetails, String synopsis) throws Exception {
        setLayout(new BorderLayout());
        setOpaque(false);
        add(createPosterAndRatingsPanel(movieDetails), BorderLayout.NORTH);
        add(createStatsAndSynopsisPanel(movieDetails, synopsis), BorderLayout.CENTER);
    }

    private JPanel createStatsAndSynopsisPanel(MovieDetails movieDetails, String synopsis) {

        JPanel statsAndSynopsis = new JPanel(new BorderLayout());
        statsAndSynopsis.setOpaque(false);
        statsAndSynopsis.add(createMovieStatsPanel(movieDetails), BorderLayout.NORTH);
        statsAndSynopsis.add(createSynopsisPanel(synopsis), BorderLayout.CENTER);
        return  statsAndSynopsis;
    }

    private JPanel createPosterAndRatingsPanel(MovieDetails movieDetails) throws IOException {
        final JPanel posterAndRatingPanel = new JPanel();
        posterAndRatingPanel.setOpaque(false);
        posterAndRatingPanel.setLayout(new BoxLayout(posterAndRatingPanel, BoxLayout.X_AXIS));
        final AspectRatioPreservedImagePanel poster = getPosterPanel(movieDetails.getPosters().getOriginal());
        posterAndRatingPanel.add(poster);
        posterAndRatingPanel.add(getRatingsPanel(movieDetails.getRatings()));
        return posterAndRatingPanel;
    }

    private Component createMovieStatsPanel(MovieDetails movieDetails) {
        JPanel statsPanel = new JPanel();
        statsPanel.setOpaque(false);
        final JLabel genresPanel = getGenresPanel(movieDetails);
        genresPanel.setHorizontalAlignment(SwingConstants.LEFT);
        statsPanel.add(genresPanel);
        final JLabel dateLabel = getDetailsLabel(movieDetails.getYear());
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statsPanel.add(dateLabel);
        return statsPanel;
    }

    private JLabel getGenresPanel(MovieDetails movieDetails) {
        StringBuilder genres = new StringBuilder();
        for (int i = 0; i < movieDetails.getGenres().length; i++) {
            genres.append(movieDetails.getGenres()[i]);
            if (i < movieDetails.getGenres().length - 1) {
                genres.append(", ");
            }
        }
        return getDetailsLabel(genres.toString());
    }

    private JLabel getDetailsLabel(String label) {
        JLabel jlabel = new JLabel(label);
        jlabel.setFont(STATS_FONT);
        jlabel.setOpaque(false);
        jlabel.setForeground(Color.YELLOW);
        jlabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(jlabel);
        return jlabel;
    }

    private JComponent createSynopsisPanel(String synopsis) {
        final JTextArea synopsisPanel = new JTextArea(synopsis);
        synopsisPanel.setFont(SYNOPSIS_FONT);
        synopsisPanel.setForeground(Color.LIGHT_GRAY);
        synopsisPanel.setOpaque(false);
        synopsisPanel.setLineWrap(true);
        synopsisPanel.setWrapStyleWord(true);
        final JScrollPane jScrollPane = new JScrollPane(synopsisPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setOpaque(false);
        jScrollPane.getViewport().setOpaque(false);
        jScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));
        return jScrollPane;
    }

    private AspectRatioPreservedImagePanel getPosterPanel(String original) throws IOException {
        AspectRatioPreservedImagePanel moviePoster = new AspectRatioPreservedImagePanel(ImageIO.read(new URL(original)));
        moviePoster.setPreferredSize(new Dimension(80, 120));
        moviePoster.setOpaque(false);
        return moviePoster;
    }

    public Component getRatingsPanel(Ratings ratings) {
        BufferedImage ratingImage = null;
        JComponent ratingsLabel;
        int criticsScore = parseScore(ratings.getCriticsScore());
        boolean validCriticsScore = criticsScore >= 0;
        if (validCriticsScore) {
            ratingImage = getImageForScore(criticsScore);
        } else {
            int audienceScore = parseScore(ratings.getAudienceScore());
            if (audienceScore > 0) {
                ratingImage = getImageForScore(audienceScore);
            }
        }

        if (ratingImage != null) {
            ratingsLabel = new ImagePanel(ratingImage);
        } else {
            ratingsLabel = new JLabel("No rating");
        }

        ratingsLabel.setForeground(Color.YELLOW);
        ratingsLabel.setFont(STATS_FONT);
        ratingsLabel.setOpaque(false);
        return ratingsLabel;
    }

    public int parseScore(String score)  {
        int parsedScore = 0;
        if (score != null) {
            try {
                parsedScore = Integer.parseInt(score);

            } catch (NumberFormatException nfe) {
                parsedScore = 0;
            }
        }
        return parsedScore;
    }

    public BufferedImage getImageForScore(int score) {
        return score > 50 ? MovieDetailsPanel.FRESH_IMAGE : MovieDetailsPanel.SPLASH_IMAGE;
    }
}
