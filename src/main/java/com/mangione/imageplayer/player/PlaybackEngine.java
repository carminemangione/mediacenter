package com.mangione.imageplayer.player;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Schedules the advance to the next item. Images use an adjustable duration; videos run for their own length.
 */
public class PlaybackEngine {
    private static final Duration DEFAULT_IMAGE_DURATION = Duration.ofSeconds(5);
    private static final Duration SPEED_STEP = Duration.ofMillis(500);
    private static final Duration MIN_IMAGE_DURATION = Duration.ofSeconds(1);

    private final ScheduledExecutorService scheduler;
    private Duration imageDuration = DEFAULT_IMAGE_DURATION;
    private ScheduledFuture<?> pending;

    public PlaybackEngine(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
    }

    synchronized void scheduleAfter(Optional<Duration> naturalDuration, Runnable onExpired) {
        cancel();
        long delayMillis = naturalDuration.orElse(imageDuration).toMillis();
        pending = scheduler.schedule(onExpired, delayMillis, TimeUnit.MILLISECONDS);
    }

    synchronized void cancel() {
        if (pending != null) {
            pending.cancel(false);
            pending = null;
        }
    }

    synchronized void faster() {
        Duration shorter = imageDuration.minus(SPEED_STEP);
        imageDuration = shorter.compareTo(MIN_IMAGE_DURATION) < 0 ? MIN_IMAGE_DURATION : shorter;
    }

    synchronized void slower() {
        imageDuration = imageDuration.plus(SPEED_STEP);
    }
}
