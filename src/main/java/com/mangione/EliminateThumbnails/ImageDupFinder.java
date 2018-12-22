package com.mangione.EliminateThumbnails;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ImageDupFinder {
    private static List<SortableFile> imageFiles = new ArrayList<>();

    public static void main(String[] args) {

        File currentFile = new File("/Users/carmine/Pictures/internet/allpics");
        recurseAndCollectFiles(currentFile);
        currentFile = new File("/Users/carmine/Pictures/Gallery Grabber");
        recurseAndCollectFiles(currentFile);
        imageFiles = imageFiles.stream()
                .filter(sortableFile -> getFile(sortableFile).contains("512") || getFile(sortableFile).contains("original"))
                .collect(Collectors.toList());
        imageFiles.sort(Comparator.comparing(o -> o.file.getName()));
        System.out.println("processing " + imageFiles.size());

        for (int i = 1; i < imageFiles.size(); i++) {
            final File first = imageFiles.get(i - 1).file;
            final File second = imageFiles.get(i).file;
            if (thumbnailAndOriginal(first, second)) {
                File toDelete = first.getName().contains("512") ? first : second;
                toDelete.delete()                                                                            ;
            }
        }


    }

    private static String getFile(SortableFile file) {
        return file.file.getName();
    }

    private static void recurseAndCollectFiles(File currentFile) {
        if (currentFile != null) {
            final File[] files = currentFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        recurseAndCollectFiles(file);
                    } else {
                        if (!file.getName().startsWith(".DS"))
                            imageFiles.add(new SortableFile(file));
                    }
                }
            }
        }
    }

    private static class SortableFile implements Comparable<SortableFile> {
        private final File file;

        private SortableFile(File file) {
            this.file = file;
        }

        @Override
        public int compareTo(SortableFile o) {
            return file.getName().compareTo(o.file.getName());
        }
    }

    private static boolean thumbnailAndOriginal(File one, File two) {
        return !one.getAbsolutePath().equals(two.getAbsolutePath())
                &&
                one.getName().replace("512", "").equals(two.getName().replace("original", ""));
    }

    private static String replaceThumbnailOriginal(String filename) {
        return filename.replace("512", "").replace("original", "");
    }

}
