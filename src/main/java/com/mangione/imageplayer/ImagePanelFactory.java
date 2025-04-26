package com.mangione.imageplayer;

import javax.swing.*;

public class ImagePanelFactory {
	private final JComponent imagePanel;

	ImagePanelFactory(String imageToLoad) throws Exception {
		if (imageToLoad.endsWith("mp4"))
			imagePanel = new VideoPanel(imageToLoad).getPanel();
		else
			imagePanel = new SwingImagePanel(imageToLoad);
	}

	JComponent getImagePanel() {
		return imagePanel;
	}
}
