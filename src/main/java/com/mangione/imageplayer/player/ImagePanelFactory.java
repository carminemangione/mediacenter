package com.mangione.imageplayer.player;

import com.mangione.imageplayer.swingimage.SwingImageControllerFactory;
import com.mangione.imageplayer.video.VideoControllerFactory;

import java.util.HashMap;
import java.util.Map;

class ImagePanelFactory {
    private static final PlayerPanelFactory DEFAULT_FACTORY = new SwingImageControllerFactory();
    private static final Map<String, PlayerPanelFactory> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put("mp4", new VideoControllerFactory());
    }

    static PlayerPanel create(String filePath) throws Exception {
        String extension = filePath.substring(filePath.lastIndexOf('.') + 1).toLowerCase();
        return FACTORIES.getOrDefault(extension, DEFAULT_FACTORY).create(filePath);
    }
}
