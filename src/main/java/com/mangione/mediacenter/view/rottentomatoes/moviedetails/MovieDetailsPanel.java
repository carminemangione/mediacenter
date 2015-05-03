package com.mangione.mediacenter.view.rottentomatoes.moviedetails;

import com.mangione.mediacenter.model.rottentomatoes.moviedetails.DetailsAndSynopsis;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.MovieDetails;
import com.mangione.mediacenter.model.rottentomatoes.moviedetails.Ratings;
import com.mangione.mediacenter.view.ImagePanel;
import com.mangione.mediacenter.view.SharedConstants;
import com.mangione.mediacenter.view.components.AspectRatioPreservedImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MovieDetailsPanel extends JPanel {

    private static final BufferedImage FRESH_IMAGE;
    private static final BufferedImage SPLASH_IMAGE;
    private static final int MARQUIS_WIDTH = 140;

    static {

        try {
            FRESH_IMAGE = ImageIO.read(MovieDetailsPanel.class.getClassLoader().getResourceAsStream("rottentomatoes/fresh.png"));
            SPLASH_IMAGE = ImageIO.read(MovieDetailsPanel.class.getClassLoader().getResourceAsStream("rottentomatoes/splash.png"));
        } catch (IOException e) {
            throw new RuntimeException("Could not load rotten tomato images", e);
        }
    }

    public MovieDetailsPanel(DetailsAndSynopsis detailsAndSynopsis) throws Exception {

        setLayout(new BorderLayout());
        setOpaque(false);
        add(createPosterAndRatingsPanel(detailsAndSynopsis.getMovieDetails()), BorderLayout.NORTH);
        add(createStatsAndSynopsisPanel(detailsAndSynopsis), BorderLayout.CENTER);

    }

    private JPanel createStatsAndSynopsisPanel(DetailsAndSynopsis detailsAndSynopsis) {

        JPanel statsAndSynopsis = new JPanel(new BorderLayout());
        statsAndSynopsis.setOpaque(false);
        statsAndSynopsis.add(createMovieStatsPanel(detailsAndSynopsis.getMovieDetails()), BorderLayout.NORTH);
        statsAndSynopsis.add(createSynopsisPanel(detailsAndSynopsis.getSynopsis().getSynopsis()), BorderLayout.CENTER);
        return statsAndSynopsis;
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
        final JPanel genresPanel = getGenresPanel(movieDetails);
        statsPanel.add(genresPanel);
        final JLabel dateLabel = getDetailsLabel(movieDetails.getYear());
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        statsPanel.add(dateLabel);
        return statsPanel;
    }

    private JPanel getGenresPanel(MovieDetails movieDetails) {
        StringBuilder genres = new StringBuilder();
        for (int i = 0; i < movieDetails.getGenres().length; i++) {
            genres.append(movieDetails.getGenres()[i]);
            if (i < movieDetails.getGenres().length - 1) {
                genres.append(", ");
            }
        }
        final MarqueePanel marqueePanel = new MarqueePanel(genres.toString(), 30);
        marqueePanel.start();
        return marqueePanel;
    }

    private JLabel getDetailsLabel(String label) {
        JLabel jlabel = new JLabel(label);
        jlabel.setFont(SharedConstants.STATS_FONT);
        jlabel.setOpaque(false);
        jlabel.setForeground(Color.YELLOW);
        jlabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(jlabel);
        return jlabel;
    }

    private JComponent createSynopsisPanel(String synopsis) {
        final JTextArea synopsisPanel = new JTextArea(synopsis);
        synopsisPanel.setFont(SharedConstants.SYNOPSIS_FONT);
        synopsisPanel.setForeground(SharedConstants.LIGHT_TEXT_COLOR);
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

    private AspectRatioPreservedImagePanel getPosterPanel(String original) {
        AspectRatioPreservedImagePanel moviePoster = null;
        try {
            moviePoster = new AspectRatioPreservedImagePanel(ImageIO.read(new URL(original)));
            moviePoster.setPreferredSize(new Dimension(80, 120));
            moviePoster.setOpaque(false);
        } catch (IOException e) {
            System.out.println("Could not load image: " + original);
        }
        return moviePoster;
    }

    private Component getRatingsPanel(Ratings ratings) {
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
        ratingsLabel.setFont(SharedConstants.STATS_FONT);
        ratingsLabel.setOpaque(false);
        return ratingsLabel;
    }

    private int parseScore(String score) {
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

    private BufferedImage getImageForScore(int score) {
        return score > 50 ? MovieDetailsPanel.FRESH_IMAGE : MovieDetailsPanel.SPLASH_IMAGE;
    }

    class MarqueePanel extends JPanel implements ActionListener {

        private static final int RATE = 6;
        private final Timer timer = new Timer(1000 / RATE, this);
        private final JLabel label = new JLabel();
        private final String s;
        private final int n;
        private int index;

        public MarqueePanel(String stringToScroll, int numberOfDisplayedCharacters) {
            if (stringToScroll == null || numberOfDisplayedCharacters < 1) {
                throw new IllegalArgumentException("Null string or n < 1");
            }
            StringBuilder sb = new StringBuilder(numberOfDisplayedCharacters);
            for (int i = 0; i < numberOfDisplayedCharacters; i++) {
                sb.append(' ');
            }
            this.s = sb + stringToScroll + sb;
            this.n = numberOfDisplayedCharacters;
            label.setFont(SharedConstants.STATS_FONT);
            label.setOpaque(false);
            label.setForeground(Color.YELLOW);
            label.setText(sb.toString());
            this.add(label);
            setOpaque(false);

            label.setPreferredSize(new Dimension(MARQUIS_WIDTH, label.getPreferredSize().height));
        }

        public void start() {
            timer.start();
        }


        @Override
        public void actionPerformed(ActionEvent e) {
            index++;
            if (index > s.length() - n) {
                index = 0;
            }
            label.setText(s.substring(index, index + n));
        }
    }
}


