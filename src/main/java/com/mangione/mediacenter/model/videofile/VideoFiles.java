package com.mangione.mediacenter.model.videofile;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * User: carminemangione
 * Date: Oct 7, 2009
 * Time: 10:31:15 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class VideoFiles {
    private final VideoFile[] videoFiles;
    private static final int NUMBER_OF_IMAGES_TO_PRELOAD = 1;

    public VideoFiles(String[] videoDirectories) {
        final List<VideoFile> videoFiles = Collections.synchronizedList(new ArrayList<>());
        for (String directory : videoDirectories) {
            if (directory.length() > 0) {
                final File staringDirectory = new File(directory);
                travelDownDirectoryTreeLookingForVideoFiles(staringDirectory, videoFiles);
            }
        }
        this.videoFiles = videoFiles.toArray(new VideoFile[videoFiles.size()]);
        Arrays.sort(this.videoFiles);

    }

    public synchronized VideoFile getVideoFile(int index) {
        for (int i = Math.max(index, 0); i < Math.min(videoFiles.length, index + NUMBER_OF_IMAGES_TO_PRELOAD); i++) {
            videoFiles[i].getImageIcon();
        }
        return videoFiles[index];
    }

    public synchronized int getIndexFirstVideoStart(char starting) {
        boolean found = false;
        int i;
        for (i = 0; i < videoFiles.length && !found; i++) {
            char charValueAtStartOfName = videoFiles[i].getVideoName().toLowerCase().charAt(0);
            found = starting <= charValueAtStartOfName;

        }
        return i;
    }

    public synchronized int getNumberOfVideoFiles() {
        return videoFiles.length;
    }

    private static void travelDownDirectoryTreeLookingForVideoFiles(File currentDirectory, List<VideoFile> videoFiles) {
        File imageFile = null;
        VideoFile foundFile = null;
        if (currentDirectory.isDirectory()) {
            File[] files = currentDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (file.getName().toUpperCase().contains("VIDEO_TS")) {
                            foundFile = new DvdVideoFile(file);
                        } else {
                            travelDownDirectoryTreeLookingForVideoFiles(file, videoFiles);
                        }

                    } else {
                        String name = file.getName();
                        if (name.endsWith(".mkv") || name.endsWith(".mp4") || name.endsWith(".m4v")) {
                            foundFile = new VideoFile(currentDirectory, name);
                        } else if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif")) {
                            imageFile = file;
                        }
                    }
                }

                if (foundFile != null) {
                    foundFile.setImageFile(imageFile);
                    videoFiles.add(foundFile);
                }
            }
        }
    }


}
