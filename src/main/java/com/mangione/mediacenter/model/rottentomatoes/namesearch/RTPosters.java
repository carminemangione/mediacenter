package com.mangione.mediacenter.model.rottentomatoes.namesearch;

public class RTPosters {

    private final String thumbnail;
    private final String profile;

    public RTPosters(String thumbnail, String profile) throws Exception {
        this.thumbnail = thumbnail;
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
