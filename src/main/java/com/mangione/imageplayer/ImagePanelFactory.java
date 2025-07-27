package com.mangione.imageplayer;

public class ImagePanelFactory {
	private final PlayerPanel playerPanel;

	ImagePanelFactory(String imageToLoad) throws Exception {
		if (imageToLoad.endsWith("mp4"))
			playerPanel = new VideoPanel(imageToLoad);
		else
			playerPanel = new SwingImagePanel(imageToLoad);
	}

	PlayerPanel getPlayerPanel() {
		return playerPanel;
	}
}
