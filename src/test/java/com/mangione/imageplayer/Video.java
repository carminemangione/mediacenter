package com.mangione.imageplayer;

import com.mangione.imageplayer.video.VideoController;
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
		try {
			VideoController controller = new VideoController(
					"/Volumes/Pictures/allpics/20221211/qNaGJRS9EBhJ639TOuIvQZCu1JGsK-Iv_7_V9CQFlVA.gif.mp4");
			SwingUtilities.invokeLater(() -> jframe.add(controller.getPanel(), BorderLayout.CENTER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}