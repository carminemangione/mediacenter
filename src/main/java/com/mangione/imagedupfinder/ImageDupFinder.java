package com.mangione.imagedupfinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: carminemangione
 * Date: Jul 26, 2009
 * Time: 2:35:52 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class ImageDupFinder {
    private static List<File> imageFiles = new ArrayList<>();

    public static void main(String[] args) {

        File currentFile = new File("/Users/carmine/Pictures/internet/allpics");
        recurseAndCollectFiles(currentFile);
        currentFile = new File("/Users/carmine/Pictures/Gallery Grabber");
        recurseAndCollectFiles(currentFile);
        System.out.println("processing " + imageFiles.size());


        ImageStats[] imageStats = new ImageStats[imageFiles.size()];
        for (int i = 0; i < imageFiles.size(); i++) {
            System.out.println("Processing file " + i + ": " + imageFiles.get(i).getName());
            try {
                imageStats[i] = new ImageStats(imageFiles.get(i));
            } catch (Throwable e) {
                //noinspection ResultOfMethodCallIgnored
                imageFiles.get(i).delete();
            }
        }

        System.out.println("Starting comparisons");
        for (int i = 0; i < imageStats.length; i++) {
            ImageStats imageStat = imageStats[i];
            for (int j = i + 1; j < imageStats.length; j++) {
                ImageStats other = imageStats[j];
                if (imageStat != null && other != null && imageStat.match(98, other) && i != j) {
                    if (other.getImageFile().delete()) {
                        imageStats[j] = null;
                        System.out.println("Successfully deleted file: " + other.getImageFile().getName());
                    } else {
                        System.out.println("Error deleting file: " + other.getImageFile().getName());
                    }
                }

            }
        }
    }

    private static void recurseAndCollectFiles(File currentFile) {
        if (currentFile != null) {
            for (File file : currentFile.listFiles()) {
                if (file.isDirectory()) {
                    recurseAndCollectFiles(file);
                } else {
                    imageFiles.add(file);
                }
            }
        }
    }

}
