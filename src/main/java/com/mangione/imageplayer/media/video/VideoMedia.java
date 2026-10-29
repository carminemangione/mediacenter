package com.mangione.imageplayer.media.video;

import com.mangione.imageplayer.media.PlayableMedia;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioTrack;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.io.File;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;

public class VideoMedia implements PlayableMedia {
    private final VideoPanel panel;
    private final MediaPlayer player;
    private final CompletableFuture<Void> ready = new CompletableFuture<>();
    private volatile Duration naturalDuration;
    private volatile boolean hasAudio;

    static {
        Platform.setImplicitExit(false);
    }

    public VideoMedia(File file) throws Exception {
        panel = createPanel(); // constructing a JFXPanel starts the JavaFX toolkit
        Media media = new Media(file.toURI().toString());

        // Built on the JavaFX thread, which also delivers status events, so the handlers are attached before any can fire
        CompletableFuture<MediaPlayer> created = new CompletableFuture<>();
        Platform.runLater(() -> {
            try {
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setMute(true);
                mediaPlayer.setOnReady(() -> onReady(mediaPlayer, media));
                mediaPlayer.setOnError(() -> ready.completeExceptionally(mediaPlayer.getError()));
                panel.setScene(sceneFor(mediaPlayer));
                mediaPlayer.play();
                created.complete(mediaPlayer);
            } catch (RuntimeException e) {
                created.completeExceptionally(e);
            }
        });
        player = created.get();
    }

    private static VideoPanel createPanel() throws Exception {
        AtomicReference<VideoPanel> created = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> created.set(new VideoPanel()));
        return created.get();
    }

    private static Scene sceneFor(MediaPlayer mediaPlayer) {
        MediaView viewer = new MediaView(mediaPlayer);
        viewer.setPreserveRatio(true);
        viewer.fitWidthProperty().bind(Bindings.selectDouble(viewer.sceneProperty(), "width"));
        viewer.fitHeightProperty().bind(Bindings.selectDouble(viewer.sceneProperty(), "height"));

        StackPane root = new StackPane();
        root.getChildren().add(viewer);
        return new Scene(root, 500, 500, Color.BLACK);
    }

    private void onReady(MediaPlayer mediaPlayer, Media media) {
        javafx.util.Duration total = mediaPlayer.getTotalDuration();
        if (!total.isUnknown() && !total.isIndefinite()) {
            naturalDuration = Duration.ofMillis((long) total.toMillis());
        }
        hasAudio = media.getTracks().stream().anyMatch(AudioTrack.class::isInstance);
        ready.complete(null);
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }

    @Override
    public CompletionStage<Void> ready() {
        return ready;
    }

    @Override
    public Optional<Duration> getNaturalDuration() {
        return Optional.ofNullable(naturalDuration);
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
