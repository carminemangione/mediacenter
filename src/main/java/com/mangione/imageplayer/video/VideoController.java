package com.mangione.imageplayer.video;

import com.mangione.imageplayer.player.PlayerPanel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioTrack;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class VideoController implements PlayerPanel {
    private final VideoPanel panel;
    private final MediaPlayer player;
    private Duration totalDuration;
    private volatile boolean hasAudio;

    public VideoController(String filePath) throws Exception {
        Platform.setImplicitExit(false);
        panel = new VideoPanel();

        Media media = new Media(new File(filePath).toURI().toURL().toString());
        player = new MediaPlayer(media);
        player.setMute(true);

        MediaView viewer = new MediaView(player);
        viewer.setPreserveRatio(true);

        CountDownLatch readyLatch = new CountDownLatch(1);
        player.setOnReady(() -> {
            totalDuration = player.getTotalDuration();
            hasAudio = media.getTracks().stream().anyMatch(track -> track instanceof AudioTrack);
            readyLatch.countDown();
        });

        DoubleProperty width = viewer.fitWidthProperty();
        DoubleProperty height = viewer.fitHeightProperty();
        width.bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
        height.bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));

        StackPane root = new StackPane();
        root.getChildren().add(viewer);
        Scene scene = new Scene(root, 500, 500, Color.BLACK);

        readyLatch.await(5, TimeUnit.SECONDS);

        panel.setScene(scene);
        player.play();
    }

    @Override
    public JComponent getPanel() {
        return panel;
    }

    @Override
    public int getDurationMillis() {
        return (int) totalDuration.toMillis();
    }

    @Override
    public boolean hasAudio() {
        return hasAudio;
    }

    @Override
    public void setMuted(boolean muted) {
        Platform.runLater(() -> player.setMute(muted));
    }

    @Override
    public void dispose() {
        Platform.runLater(player::dispose);
    }
}
