package com.mangione.imageplayer;

import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Video {
	private String Dir = System.getProperty("user.dir");
	private final JFrame jframe;

	public static void main(String[] args) {
		new Video();
	}

	private Video() {
		jframe = new JFrame();
		jframe.setLayout(new BorderLayout());
		start();
		Platform.runLater(() -> {
			jframe.pack();
			jframe.setVisible(true);
			jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		});
	}

	public void start() {
		final File file = new File("/Users/carmine/projects/imageplayer/src/test/resources/ThirstyDrearySwan-mobile.mp4");
		final VideoPanel videoPanel = new VideoPanel(file);
		SwingUtilities.invokeLater(() -> jframe.add(videoPanel.getPanel(), BorderLayout.CENTER));
	}
}