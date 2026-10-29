package com.mangione.imageplayer.player;

import com.mangione.imageplayer.file.Playlist;
import com.mangione.imageplayer.file.PlaylistEntry;
import com.mangione.imageplayer.media.MediaTypeRegistry;
import com.mangione.imageplayer.media.PlayableMedia;

import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Moves through the playlist and shows each item. All loading runs on the worker so the Swing thread never blocks.
 */
public class ImagePlayerController {
    private static final Duration SKIP_DELAY_AFTER_ERROR = Duration.ofMillis(500);
    private static final Duration READY_TIMEOUT = Duration.ofSeconds(30);

    private final Playlist playlist;
    private final MediaTypeRegistry mediaTypes;
    private final MediaDisplay display;
    private final PlaybackEngine engine;
    private final Executor worker;
    private PlaylistEntry showing; // guarded by this

    public ImagePlayerController(Playlist playlist, MediaTypeRegistry mediaTypes, MediaDisplay display,
                                 PlaybackEngine engine, Executor worker) {
        this.playlist = playlist;
        this.mediaTypes = mediaTypes;
        this.display = display;
        this.engine = engine;
        this.worker = worker;
    }

    public void start() {
        worker.execute(() -> {
            try {
                show(playlist.awaitCurrent());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public KeyBindings keyBindings() {
        return new KeyBindings()
                .bind(KeyEvent.VK_EQUALS, engine::faster)
                .bind(KeyEvent.VK_MINUS, engine::slower)
                .bind(KeyEvent.VK_COMMA, () -> navigate(playlist::previous))
                .bind(KeyEvent.VK_PERIOD, () -> navigate(playlist::next))
                .bind(KeyEvent.VK_R, () -> navigate(playlist::jumpToRandom))
                .bind(KeyEvent.VK_D, () -> navigate(this::deleteCurrent));
    }

    private void navigate(Supplier<Optional<PlaylistEntry>> move) {
        worker.execute(() -> move.get().ifPresent(this::show));
    }

    // A timer that already fired can't be cancelled, so it only advances if nothing else was shown since
    private void advanceFrom(PlaylistEntry from) {
        synchronized (this) {
            if (from != showing) return;
        }
        playlist.next().ifPresent(this::show);
    }

    private synchronized void scheduleAdvance(PlaylistEntry from, Optional<Duration> delay) {
        if (from == showing) {
            engine.scheduleAfter(delay, () -> advanceFrom(from));
        }
    }

    private Optional<PlaylistEntry> deleteCurrent() {
        return playlist.current()
                .filter(entry -> entry.file().delete())
                .flatMap(entry -> playlist.remove(entry.mediaFile()));
    }

    private void show(PlaylistEntry entry) {
        synchronized (this) {
            showing = entry;
            engine.cancel();
        }
        try {
            PlayableMedia media = mediaTypes.create(entry.file());
            display.display(media, describe(entry));
            // The timer starts once a video knows its length; one that never gets ready is skipped
            media.ready().toCompletableFuture().copy()
                    .orTimeout(READY_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS)
                    .whenComplete((ignored, error) -> {
                        if (error == null) {
                            scheduleAdvance(entry, media.getNaturalDuration());
                        } else {
                            //noinspection CallToPrintStackTrace
                            error.printStackTrace();
                            scheduleAdvance(entry, Optional.of(SKIP_DELAY_AFTER_ERROR));
                        }
                    });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            scheduleAdvance(entry, Optional.of(SKIP_DELAY_AFTER_ERROR));
        }
    }

    private static String describe(PlaylistEntry entry) {
        return String.format("%d of %d: %s", entry.position() + 1, entry.size(), entry.file().getName());
    }
}
