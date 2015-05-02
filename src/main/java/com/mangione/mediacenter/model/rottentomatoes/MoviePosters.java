package com.mangione.mediacenter.model.rottentomatoes;

import java.io.Serializable;

public class MoviePosters implements Serializable {

    private final String profile;
    private final String original;

    public MoviePosters(String profile, String original) {
        this.profile = profile;
        this.original = original;
    }


    public String getProfile() {
        return profile;
    }

    public String getOriginal() {
        return original;
    }

}
