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
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class VideoPanel implements PlayerPanel {

	private final JFXPanel jfxPanel;
	private Duration totalDuration;

	public VideoPanel(String file) throws Exception {
		Platform.setImplicitExit(false);
		jfxPanel = new JFXPanel();
		File file1 = new File(file);
		Media media = new Media(file1.toURI().toURL().toString());
		AtomicBoolean ready = new AtomicBoolean(false);
		MediaPlayer player = new MediaPlayer(media);
		player.setAutoPlay(true);
		MediaView viewer = new MediaView(player);
		player.setMute(true);
		viewer.setPreserveRatio(true);

		StackPane root = new StackPane();
		root.getChildren().add(viewer);

		player.setOnReady(() -> {
			ready.set(true);
		});

		//noinspection StatementWithEmptyBody
		while (!ready.get());
		System.out.println("Player Status: " + player.getStatus());
		DoubleProperty width = viewer.fitWidthProperty();
		DoubleProperty height = viewer.fitHeightProperty();
		width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
		height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));
		viewer.setPreserveRatio(true);
		//set the Scene
		Scene scenes = new Scene(root, 500, 500, Color.BLACK);
		startPlayer(player, scenes);
	}

	private void startPlayer(MediaPlayer player, Scene scenes) {
		totalDuration = player.getTotalDuration();
		player.setMute(true);
		player.play();

		jfxPanel.setScene(scenes);
		jfxPanel.setPreferredSize(new Dimension(400, 400));
	}

	@Override
	public JComponent getPanel() {
		return jfxPanel;
	}

	@Override
	public int getDurationMillis() {
		return (int) totalDuration.toMillis();
	}

}
