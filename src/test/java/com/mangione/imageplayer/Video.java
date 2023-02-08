package com.mangione.imageplayer;

import java.awt.*;

import javax.swing.*;

import javafx.application.Platform;

public class Video {
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
		final VideoPanel videoPanel = new VideoPanel(
				"/Volumes/Pictures/allpics/20221211/qNaGJRS9EBhJ639TOuIvQZCu1JGsK-Iv_7_V9CQFlVA.gif.mp4");
		SwingUtilities.invokeLater(() -> jframe.add(videoPanel.getPanel(), BorderLayout.CENTER));
	}
}