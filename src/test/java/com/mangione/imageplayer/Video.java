package com.mangione.imageplayer;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Video {
	private String Dir = System.getProperty("user.dir");
	private final JFrame jframe;

	public static void main(String[] args) throws Exception {
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
		JFXPanel jfxPanel = new JFXPanel();
		//Converts media to string URL
		Media media = new Media(new File(
				"/Users/carmine/projects/imageplayer/src/test/resources/MetallicLongBat-mobile.mp4").toURI().toString());
		MediaPlayer player = new MediaPlayer(media);
		MediaView viewer = new MediaView(player);

		//change width and height to fit video
		viewer.setPreserveRatio(true);

		StackPane root = new StackPane();
		root.getChildren().add(viewer);

		Platform.runLater(() -> {
			DoubleProperty width = viewer.fitWidthProperty();
			DoubleProperty height = viewer.fitHeightProperty();
			width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
			height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
			viewer.setPreserveRatio(true);
			//set the Scene
			Scene scenes = new Scene(root, 500, 500, Color.BLACK);
			player.play();
			player.setOnEndOfMedia(new Runnable() {
				@Override
				public void run() {
					player.stop();
				}
			});

			jfxPanel.setScene(scenes);
			jfxPanel.setPreferredSize(new Dimension(400, 400));
			jframe.add(jfxPanel, BorderLayout.CENTER);
		});

	}
}