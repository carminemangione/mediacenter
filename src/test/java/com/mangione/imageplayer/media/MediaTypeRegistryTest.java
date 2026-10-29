package com.mangione.imageplayer.media;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MediaTypeRegistryTest {
    private final PlayableMediaFactory factory = file -> null;
    private final MediaTypeRegistry registry = new MediaTypeRegistry().register(factory, "jpg", "mp4");

    @Test
    void matchesExtensionsIgnoringCase() {
        assertTrue(registry.isSupported(new File("a.jpg")));
        assertTrue(registry.isSupported(new File("a.JPG")));
        assertTrue(registry.isSupported(new File("a.gif.mp4")));
    }

    @Test
    void rejectsUnregisteredAndMissingExtensions() {
        assertFalse(registry.isSupported(new File("loadedDirectories.json")));
        assertFalse(registry.isSupported(new File("README")));
    }

    @Test
    void createFailsForUnsupportedFile() {
        assertThrows(IllegalArgumentException.class, () -> registry.create(new File("a.txt")));
    }
}
