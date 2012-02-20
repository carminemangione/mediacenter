package com.mangione.mediacenter.model.videofile;

import java.io.File;

/**
 * User: carminemangione
 * Date: Oct 30, 2009
 * Time: 11:19:52 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class DvdVideoFile extends VideoFile {

    public DvdVideoFile(File currentDirectory, File imdbFile) {
        super(currentDirectory, currentDirectory.getPath().substring(currentDirectory.getPath().lastIndexOf("/")), imdbFile);
    }

    @Override
    public String[] getLaunchMovieCommand() {
        return new String[] {"/Applications/VLC.app/Contents/MacOS/VLC",
                "dvdsimple://" + getCurrentDirectory(),
                "--fullscreen", "--video-on-top", "--force-dolby-surround=1"};

//        return new String[]{"open", "-a", "dvd player" ,
//                 getCurrentDirectory().getPath()};
    }

    @Override
    public String getApplicationName() {
        return "VLC";
    }
}
