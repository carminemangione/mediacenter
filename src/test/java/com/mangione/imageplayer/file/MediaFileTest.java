package com.mangione.imageplayer.file;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MediaFileTest {
    private static List<String> sortedNames(String... names) {
        return Stream.of(names)
                .map(name -> new MediaFile(new File("/pics", name)))
                .sorted()
                .map(MediaFile::getName)
                .toList();
    }

    @Test
    void ordersCopyNumbersNumerically() {
        assertEquals(List.of("photo1.jpg", "photo2.jpg", "photo10.jpg"),
                sortedNames("photo10.jpg", "photo2.jpg", "photo1.jpg"));
    }

    @Test
    void ignoresDashBeforeCopyNumber() {
        assertEquals(List.of("photo2.jpg", "photo-3.jpg", "photo-11.jpg"),
                sortedNames("photo-11.jpg", "photo-3.jpg", "photo2.jpg"));
    }

    @Test
    void placesOriginalBeforeItsCopies() {
        assertEquals(List.of("photo.jpg", "photo1.jpg"), sortedNames("photo1.jpg", "photo.jpg"));
    }

    @Test
    void stripsOnlyTheTrailingExtension() {
        assertEquals(List.of("clip.gif.mp4", "clip.gif2.mp4"), sortedNames("clip.gif2.mp4", "clip.gif.mp4"));
    }
}
