package com.mangione.mediacenter.model.videofile;

import java.io.File;

/**
 * User: carminemangione
 * Date: Oct 30, 2009
 * Time: 11:20:48 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class Mp4VideoFile extends VideoFile {
    private final String mp4FileName;

    public Mp4VideoFile(File currentDirectory, String fileName, File imdbFile) {
        super(currentDirectory, fileName.substring(0, fileName.length() - 4), imdbFile);
        mp4FileName = fileName;
    }

    @Override
    public String[] getLaunchMovieCommand() {
        String[] command = {"/Applications/VLC.app/Contents/MacOS/VLC",
                "file://" + getCurrentDirectory() + "/" + mp4FileName,
                "--fullscreen", "--video-on-top", "--force-dolby-surround=1"};
        for (String s : command) {
            System.out.print(s.replace(" ", "\\ ") + " ");
        }
        return command;


    }

    @Override
    public String getApplicationName() {
        return "VLC";
    }
}
