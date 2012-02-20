package com.mangione.mediacenter.model;

import java.util.prefs.Preferences;

public class VideoDirectories {
    private static final Preferences preferences = Preferences.userNodeForPackage(VideoDirectories.class);
    private static final VideoDirectories instance = new VideoDirectories();
    private static final String VIDEO_DIRECTORIES = "videoDirectories";

    private VideoDirectories() {
        String deleteDirectories = System.getProperty("deleteDirectories", "false");
        if ("true".equals(deleteDirectories)) {
            preferences.put(VIDEO_DIRECTORIES, "");
        }
    }

    public static VideoDirectories getInstance() {
        return instance;
    }

    public String[] getVideoDirectories() {
        String[] directories = new String[0];
        String directoriesString = preferences.get(VIDEO_DIRECTORIES, "");
        if (directoriesString.length() > 0) {
            directories = directoriesString.split(",");
        }
        return directories;
    }

    public void setDirectories(String[] directories) {
        StringBuilder commaDelimitedDirectories = new StringBuilder();
        for (String directory : directories) {
            if (directory.length() > 0) {
                commaDelimitedDirectories.append(directory).append(",");
            }
        }
        if (commaDelimitedDirectories.length() > 1) {
            preferences.put(VIDEO_DIRECTORIES, commaDelimitedDirectories.substring(0, commaDelimitedDirectories.length() - 1));
        }
    }
}
