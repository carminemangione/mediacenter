package com.mangione.imagedupfinder;

import java.io.File;
import java.util.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ImageThumbnailFinder {

    public static void main(String[] args) {

        File currentFile = new File("/Users/carmine/Pictures/internet/allpics");
        List<ThumbNailFinder> imageFiles = new ArrayList<>();
        recurseAndCollectFiles(currentFile, imageFiles);
        currentFile = new File("/Users/carmine/Pictures/Gallery Grabber");
        recurseAndCollectFiles(currentFile, imageFiles);

        System.out.println("processing " + imageFiles.size());

        Collections.sort(imageFiles);
        Set<ThumbNailFinder> possibleThumbs = new HashSet<>();
        for (int i = 0; i < imageFiles.size() - 1; i ++) {
            final ThumbNailFinder first = imageFiles.get(i);
            final ThumbNailFinder second = imageFiles.get(i + 1);
            if (first.sameNameButThumbnail(second)) {
                if (first.isThumb()) {
                    possibleThumbs.add(first);
                    first.getImageFile().delete();
                }
                if (second.isThumb()) {
                    possibleThumbs.add(second);
                    second.getImageFile().delete();
                }
                System.out.println(first.getImageFile().getName() + " " + second.getImageFile().getName());

            }
        }

        possibleThumbs.stream().forEach(System.out::println);
    }

    private static void recurseAndCollectFiles(File currentFile, List<ThumbNailFinder> foundFiles) {
        if (currentFile != null) {
            final File[] files = currentFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        recurseAndCollectFiles(file, foundFiles);
                    } else {
                        foundFiles.add(new ThumbNailFinder(file));
                    }
                }
            }
        }
    }

    private static class ThumbNailFinder implements Comparable<ThumbNailFinder> {
        private final File imageFile;

        private ThumbNailFinder(File imageFile) {
            this.imageFile = imageFile;
        }

        public boolean sameNameButThumbnail(ThumbNailFinder thumbNailFinder) {
            return  thumbNailFinder.getImageFile().getName().contains("thumb") || imageFile.getName().contains("thumb")  &&
                    thumbNailFinder.getImageFile().getName().replace("_thumb", "").equals(imageFile.getName().replace("_thumb", ""));
        }

        public File getImageFile() {
            return imageFile;
        }

        public boolean isThumb() {
            return imageFile.getName().contains("thumb");
        }

        @Override
        public int compareTo(ThumbNailFinder o) {
            return o.getImageFile().getName().compareTo(imageFile.getName());
        }

        @Override
        public String toString() {
            return imageFile.getName();
        }

        @Override
        public boolean equals(Object o) {
            boolean equals = false;
            if (o instanceof ThumbNailFinder) {
                equals = ((ThumbNailFinder)o).getImageFile().getPath().equals(imageFile.getPath());
            }

            return equals;
        }

        @Override
        public int hashCode() {
            return imageFile.getParent().hashCode();
        }
    }

}
