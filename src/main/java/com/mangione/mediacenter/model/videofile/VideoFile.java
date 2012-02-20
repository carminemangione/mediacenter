package com.mangione.mediacenter.model.videofile;

import javax.swing.*;
import java.io.File;

/**
 * User: carminemangione
 * Date: Oct 7, 2009
 * Time: 10:32:53 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public  class VideoFile implements Comparable<VideoFile> {
    private static final ImageIcon IMAGE_NOT_FOUND_ICON = new ImageIcon(VideoFile.class.getClassLoader().getResource("blankimage.jpeg"));

    private final String fullVideoPath;
    private final String videoName;

    private File imageFile;
    private ImageIcon imageIcon;


    public VideoFile(File currentDirectory, String videoName) {
        fullVideoPath = new File(currentDirectory, videoName).getAbsolutePath();
        this.videoName = makeVideoNameDecent(videoName);
    }

    public VideoFile(File currentDirectory) {
        final File dvdMovieDirectory = currentDirectory.getParentFile();
        fullVideoPath = dvdMovieDirectory.getAbsolutePath();
        this.videoName = makeVideoNameDecent(dvdMovieDirectory.getName());
    }

    public ImageIcon getImageIcon() {
        if (imageIcon == null) {
            if (imageFile == null) {
                imageIcon = IMAGE_NOT_FOUND_ICON;
            } else {
                imageIcon = new ImageIcon(imageFile.getPath());
            }

        }
        return imageIcon;

    }

    public String getVideoName() {
        return videoName;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String getLaunchMovieCommand() {
//        String[] command = {"/Applications/VLC.app/Contents/MacOS/VLC",
//                "file://" + getCurrentDirectory() + "/" + mp4FileName,
//                "--fullscreen", "--video-on-top", "--force-dolby-surround=1"};

        //        for (String s : command) {
//            System.out.print(s.replace(" ", "\\ ") + " ");
//        }
        return String.format("open -a MPlayerX.app --args -file %s", escapeFileName(fullVideoPath));
    }

    @Override
    public int compareTo(VideoFile videoFile) {
        return videoName.compareTo(videoFile.videoName);
    }

    private String makeVideoNameDecent(String videoName)  {
        StringBuffer mixedCaseWithPeriods = getVideoNameWithSpaces(videoName);
        String mixedCase = mixedCaseWithPeriods.toString();
        if (mixedCase.startsWith("The ")) {
            mixedCase = mixedCase.substring(4) + ", The";
        }
        return mixedCase;
    }

    private StringBuffer getVideoNameWithSpaces(String videoName) {
        String decentVideoName;
        decentVideoName = videoName.replace("_", " ");
        decentVideoName = decentVideoName.replace(".", " ");

        StringBuffer mixedCaseWithPeriods = new StringBuffer();
        boolean leadCharacter = true;
        for (int i = 0; i < decentVideoName.length(); i++) {
            char nextChar = decentVideoName.charAt(i);
            if (Character.isLetterOrDigit(nextChar)) {
                if (leadCharacter) {
                    mixedCaseWithPeriods.append(Character.toUpperCase(nextChar));
                    leadCharacter = false;
                } else {
                    mixedCaseWithPeriods.append(Character.toLowerCase(nextChar));
                }
            } else if (nextChar == '_' || nextChar == '.'|| nextChar == ' ') {
                leadCharacter = true;
                mixedCaseWithPeriods.append(" ");
            }
        }
        return mixedCaseWithPeriods;
    }

    protected String escapeFileName(String filename) {
        return filename.replace(" ", "\\ ");
    }
}
