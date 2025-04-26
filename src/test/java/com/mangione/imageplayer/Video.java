package com.mangione.imageplayer;

import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;

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
		final VideoPanel videoPanel;
		try {
			videoPanel = new VideoPanel(
					"/Volumes/Pictures/allpics/20221211/qNaGJRS9EBhJ639TOuIvQZCu1JGsK-Iv_7_V9CQFlVA.gif.mp4");
			SwingUtilities.invokeLater(() -> jframe.add(videoPanel.getPanel(), BorderLayout.CENTER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}