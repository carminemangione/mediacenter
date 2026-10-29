package com.mangione.imageplayer;

import com.mangione.imageplayer.media.PlayableMedia;
import com.mangione.imageplayer.media.video.VideoMedia;
import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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
			PlayableMedia video = new VideoMedia(new File(
					"/Volumes/Pictures/allpics/20221211/qNaGJRS9EBhJ639TOuIvQZCu1JGsK-Iv_7_V9CQFlVA.gif.mp4"));
			SwingUtilities.invokeLater(() -> jframe.add(video.getComponent(), BorderLayout.CENTER));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}