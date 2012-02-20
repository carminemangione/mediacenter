package com.mangione.mediacenter.model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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
        CSVReader csvReader = new CSVReader(new StringReader(preferences.get(VIDEO_DIRECTORIES, "")));
        String[] directories = new String[0];
        try {
            directories = csvReader.readNext();
        } catch (IOException e) {
            //ignored
        }
        return directories;
    }

    public void setDirectories(String[] directories) {
        final StringWriter stringWriter = new StringWriter();
        CSVWriter writer = new CSVWriter(stringWriter);
        writer.writeNext(directories);
        try {
            writer.close();
        } catch (IOException e) {
            //ignored
        }
        final String csvDirectories = stringWriter.toString();
        preferences.put(VIDEO_DIRECTORIES, csvDirectories);
    }
}
