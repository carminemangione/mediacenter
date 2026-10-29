package com.mangione.imageplayer.swingimage;

import com.mangione.imageplayer.player.PlayerPanel;
import com.mangione.imageplayer.player.PlayerPanelFactory;

public class SwingImageControllerFactory implements PlayerPanelFactory {
    @Override
    public PlayerPanel create(String filePath) {
        return new SwingImageController(filePath);
    }
}
