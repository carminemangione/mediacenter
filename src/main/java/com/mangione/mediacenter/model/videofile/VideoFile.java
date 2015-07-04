package com.mangione.mediacenter.model.videofile;

import com.google.common.io.ByteStreams;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;


public class VideoFile implements Comparable<VideoFile> {
    private static final BufferedImage IMAGE_NOT_FOUND_ICON;

    static {
        try {
            IMAGE_NOT_FOUND_ICON = ImageIO.read(VideoFile.class.getClassLoader().getResource("blankimage.jpeg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private final String fullVideoPath;
    private final String videoName;

    private File imageFile;
    private BufferedImage image;
    private final File currentDirectory;


    public VideoFile(File currentDirectory, String videoName) {
        this.currentDirectory = currentDirectory;
        fullVideoPath = new File(currentDirectory, videoName).getAbsolutePath();
        this.videoName = makeVideoNameDecent(videoName);
    }

    public VideoFile(File currentDirectory) {
        this.currentDirectory = currentDirectory;
        final File dvdMovieDirectory = currentDirectory.getParentFile();
        fullVideoPath = dvdMovieDirectory.getAbsolutePath();
        this.videoName = makeVideoNameDecent(dvdMovieDirectory.getName());
    }

    public BufferedImage loadImage() {
        if (image == null) {
            if (imageFile == null) {
                image = IMAGE_NOT_FOUND_ICON;
            } else {
                try {
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(imageFile.getPath()));
                    final byte[] image = ByteStreams.toByteArray(bis);
                    ByteArrayInputStream bas = new ByteArrayInputStream(image);

                    this.image = ImageIO.read(bas);
                } catch (IOException e) {
                    image = IMAGE_NOT_FOUND_ICON;
                }
            }

        }
        return image;

    }

    public String getVideoName() {
        return videoName;
    }

    public String getIdentifyingString() {
        return getCurrentDirectory() + (videoName == null ? "" : videoName);
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public String[] getLaunchMovieCommand() {
        String fileFormat = fullVideoPath.toUpperCase().contains("VIDEO_TS") ? "dvdsimple" : "file";
        return new String[]{"/Applications/VLC.app/Contents/MacOS/VLC",
                fileFormat + "://" + fullVideoPath,
                "--fullscreen", "--video-on-top", "--force-dolby-surround=1"};
//       return new String[]{"/Applications/MPlayerX.app/Contents/MacOS/MPlayerX", "--args",  "-file",  escapeFileName(fullVideoPath),
//               "-OnTopMode", "true",
//               "-StartByFullScreen", "1"};
    }

    @Override
    public int compareTo(VideoFile videoFile) {
        return videoFile != null ? videoName.compareTo(videoFile.videoName) : -1;
    }

    private String makeVideoNameDecent(String videoName) {
        videoName = stripMovieTypes(videoName);
        StringBuffer mixedCaseWithPeriods = getVideoNameWithSpaces(videoName);
        String mixedCase = mixedCaseWithPeriods.toString();
        if (mixedCase.startsWith("The ")) {
            mixedCase = mixedCase.substring(4) + ", The";
        }
        return mixedCase;
    }

    private String stripMovieTypes(String videoName) {
        return videoName.toLowerCase().replace("mp4", "").replace("mkv", "").replace("m4v", "");
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
            } else if (nextChar == '_' || nextChar == '.' || nextChar == ' ') {
                leadCharacter = true;
                mixedCaseWithPeriods.append(" ");
            }
        }
        return mixedCaseWithPeriods;
    }

    public File getCurrentDirectory() {
        return currentDirectory;
    }
}
