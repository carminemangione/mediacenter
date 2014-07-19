package com.mangione.mediacenter.model.videofile;

import java.io.File;

public class DvdVideoFile extends VideoFile {

    public DvdVideoFile(File currentDirectory) {
        super(currentDirectory);
    }

    @Override
    public String[] getLaunchMovieCommand() {
        return new String[]{"open", "-a", "dvd player",
                getCurrentDirectory().getPath()};
    }
}
