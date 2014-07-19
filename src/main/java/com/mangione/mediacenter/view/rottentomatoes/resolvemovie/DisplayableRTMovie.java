package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;

import com.mangione.mediacenter.model.rottentomatoes.namesearch.RTMovie;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

public class DisplayableRTMovie {
    private final ImageIcon posterIcon;
    private final RTMovie rtMovie;

    public String getTitle() {
        return rtMovie.getTitle();
    }

    public Long getYear() {
        return rtMovie.getYear();
    }

    public DisplayableRTMovie(RTMovie rtMovie) throws MalformedURLException {
        posterIcon = new ImageIcon(new URL(rtMovie.getPosters().getProfile()));
        this.rtMovie = rtMovie;
    }

    public ImageIcon getPosterIcon() {
        return posterIcon;
    }
}
