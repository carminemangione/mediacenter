package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class DisplayableMovie {

    private static final BufferedImage IMAGE_NOT_FOUND_ICON;

    static {
        try {
            IMAGE_NOT_FOUND_ICON = ImageIO.read(DisplayableMovie.class.getClassLoader().getResource("blankimage.jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private BufferedImage posterImage;
    private final RTMovie rtMovie;

    public String getTitle() {
        return rtMovie.getTitle();
    }

    public String getYear() {
        return rtMovie.getYear();
    }

    public DisplayableMovie(RTMovie rtMovie)  {
        try {
            posterImage = ImageIO.read(new URL(rtMovie.getPosters().getOriginal()));
        } catch (Exception e) {
            posterImage = IMAGE_NOT_FOUND_ICON;
        }
        this.rtMovie = rtMovie;
    }

    public BufferedImage getPosterImage() {
        return posterImage;
    }

 }
