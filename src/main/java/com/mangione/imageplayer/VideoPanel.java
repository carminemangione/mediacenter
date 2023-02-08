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

public class VideoPanel {

	private final JFXPanel jfxPanel;

	public VideoPanel(String file) {
		jfxPanel = new JFXPanel();
		Media media = new Media("file:" + file);
		MediaPlayer player = new MediaPlayer(media);
		MediaView viewer = new MediaView(player);
		player.setMute(true);

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
			player.setMute(true);
			//set the Scene
			Scene scenes = new Scene(root, 500, 500, Color.BLACK);
			player.play();
			player.setOnEndOfMedia(player::stop);

			jfxPanel.setScene(scenes);
			jfxPanel.setPreferredSize(new Dimension(400, 400));
		});
	}

	public JComponent getPanel() {
		return jfxPanel;
	}
}
