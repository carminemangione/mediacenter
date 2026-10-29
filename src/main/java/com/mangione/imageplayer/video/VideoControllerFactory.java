package com.mangione.imageplayer.video;

import com.mangione.imageplayer.player.PlayerPanel;
import com.mangione.imageplayer.player.PlayerPanelFactory;

public class VideoControllerFactory implements PlayerPanelFactory {
    @Override
    public PlayerPanel create(String filePath) throws Exception {
        return new VideoController(filePath);
    }
}
