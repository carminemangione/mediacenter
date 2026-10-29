package com.mangione.imageplayer.file;

import java.io.File;

public record PlaylistEntry(MediaFile mediaFile, int position, int size) {
    public File file() {
        return mediaFile.getFile();
    }
}
