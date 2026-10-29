package com.mangione.imageplayer.media;

import java.io.File;

@FunctionalInterface
public interface PlayableMediaFactory {
    PlayableMedia create(File file) throws Exception;
}
