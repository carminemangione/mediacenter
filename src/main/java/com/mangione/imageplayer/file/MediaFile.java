package com.mangione.imageplayer.file;

import java.io.File;
import java.util.Comparator;

/**
 * A media file that sorts by name while ordering copies such as "photo2" and "photo10" numerically.
 */
public class MediaFile implements Comparable<MediaFile> {
    private static final int MAX_COPY_NUMBER_DIGITS = 3;
    private static final Comparator<MediaFile> ORDER = Comparator
            .comparing((MediaFile mediaFile) -> mediaFile.baseName)
            .thenComparing(mediaFile -> mediaFile.copyNumber, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparing(mediaFile -> mediaFile.file);

    private final File file;
    private final String baseName;
    private final Long copyNumber;

    public MediaFile(File file) {
        this.file = file;
        String name = stripExtension(file.getAbsolutePath().toLowerCase()).replace("-", "");
        int digitsStart = name.length();
        while (digitsStart > 0 && Character.isDigit(name.charAt(digitsStart - 1))) {
            digitsStart--;
        }
        int digitCount = name.length() - digitsStart;
        if (digitCount >= 1 && digitCount <= MAX_COPY_NUMBER_DIGITS) {
            baseName = name.substring(0, digitsStart);
            copyNumber = Long.parseLong(name.substring(digitsStart));
        } else {
            baseName = name;
            copyNumber = null;
        }
    }

    private static String stripExtension(String path) {
        int dot = path.lastIndexOf('.');
        return dot > path.lastIndexOf(File.separatorChar) ? path.substring(0, dot) : path;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return file.getName();
    }

    @Override
    public int compareTo(MediaFile other) {
        return ORDER.compare(this, other);
    }

    @Override
    public String toString() {
        return file.getAbsolutePath();
    }
}
