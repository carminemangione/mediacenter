package com.mangione.mediacenter.model.rottentomatoes;

public class RTPosters {

    private final String thumbnail;
    private final String profile;

    public RTPosters(String thumbnail, String profile) {
        this.thumbnail = thumbnail;
        this.profile = profile;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getProfile() {
        return profile;
    }
}
