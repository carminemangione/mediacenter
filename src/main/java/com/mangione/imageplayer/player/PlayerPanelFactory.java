package com.mangione.imageplayer.player;

public interface PlayerPanelFactory {
    PlayerPanel create(String filePath) throws Exception;
}
