package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class DisplayableMovie {
    private final BufferedImage posterImage;
    private final RTMovie rtMovie;

    public String getTitle() {
        return rtMovie.getTitle();
    }

    public String getYear() {
        return rtMovie.getYear();
    }

    public DisplayableMovie(RTMovie rtMovie) throws IOException {
        posterImage = ImageIO.read(new URL(rtMovie.getPosters().getOriginal()));
        this.rtMovie = rtMovie;
    }

    public BufferedImage getPosterImage() {
        return posterImage;
    }
}
