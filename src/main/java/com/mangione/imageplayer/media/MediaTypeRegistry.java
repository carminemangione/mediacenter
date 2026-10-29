package com.mangione.imageplayer.media;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The single place that knows which file extensions are supported and how to play each one.
 */
public class MediaTypeRegistry {
    private final Map<String, PlayableMediaFactory> factories = new HashMap<>();

    public MediaTypeRegistry register(PlayableMediaFactory factory, String... extensions) {
        Arrays.stream(extensions)
                .map(String::toLowerCase)
                .forEach(extension -> factories.put(extension, factory));
        return this;
    }

    public boolean isSupported(File file) {
        return factories.containsKey(extensionOf(file));
    }

    public PlayableMedia create(File file) throws Exception {
        PlayableMediaFactory factory = factories.get(extensionOf(file));
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported media type: " + file);
        }
        return factory.create(file);
    }

    private static String extensionOf(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        return dot < 0 ? "" : name.substring(dot + 1).toLowerCase();
    }
}
