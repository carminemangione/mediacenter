package com.mangione.mediacenter.model.rottentomatoes;

public class MoviePosters {

    private final String thumbnail;
    private final String profile;
    private final String original;

    public MoviePosters(String thumbnail, String profile, String original) {
        this.thumbnail = thumbnail;
        this.profile = profile;
        this.original = original;
    }


    public String getProfile() {
        return replaceTmbToWorkAroundRTBug(this.profile, "pro");
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getOriginal() {
        return replaceTmbToWorkAroundRTBug(original, "ori");
    }

    private String replaceTmbToWorkAroundRTBug(String thumbnailUrl, String replacement) {
        return thumbnailUrl == null ? null : thumbnailUrl.replace("tmb", replacement);

    }
}
