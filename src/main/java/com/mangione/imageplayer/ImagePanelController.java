package com.mangione.imageplayer;

import javax.swing.*;
import java.awt.*;

/**
 * User: carminemangione
 * Date: Feb 23, 2009
 * Time: 2:32:11 AM
 * Copyright Cognitive Health Sciences, Inc. All rights reserved
 */
public class ImagePanelController {
	private final ImagePanel imagePanel;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			final ImagePanelController controller = new ImagePanelController("/Users/carmine/Pictures/internet/allpics/zykx8t2vb9g51.jpg");
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			final JComponent imagePanel = controller.getImagePanel();
			imagePanel.setPreferredSize(new Dimension(800, 600));
			frame.getContentPane().add(imagePanel, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		});
	}

	ImagePanelController(String initialImage) {
		imagePanel = new ImagePanel(initialImage);
	}

	public void setImage(String newImage) {
		imagePanel.setImage(newImage);
	}

	JComponent getImagePanel() {
		return imagePanel;
	}
}
