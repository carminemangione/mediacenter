package com.mangione.imageplayer.media;

import javax.swing.*;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface PlayableMedia {
    /**
     * Called on the Swing thread.
     */
    JComponent getComponent();

    /**
     * Completes once getNaturalDuration and hasAudio are known.
     */
    default CompletionStage<Void> ready() {
        return CompletableFuture.completedFuture(null);
    }

    /**
     * How long the media runs on its own, such as a video's length; empty means use the slideshow's image duration.
     */
    Optional<Duration> getNaturalDuration();

    boolean hasAudio();

    void setMuted(boolean muted);

    default void dispose() {
    }
}
