package com.mangione.imageplayer.file;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PlaylistTest {
    private static final Random FIRST_INDEX = new Random() {
        @Override
        public int nextInt(int bound) {
            return 0;
        }
    };

    private static List<MediaFile> files(String... names) {
        return Stream.of(names).map(name -> new MediaFile(new File("/pics", name))).toList();
    }

    private static String nameOf(Optional<PlaylistEntry> entry) {
        return entry.orElseThrow().file().getName();
    }

    @Test
    void emptyPlaylistHasNoEntries() {
        Playlist playlist = new Playlist(FIRST_INDEX);
        assertTrue(playlist.current().isEmpty());
        assertTrue(playlist.next().isEmpty());
        assertTrue(playlist.previous().isEmpty());
        assertTrue(playlist.jumpToRandom().isEmpty());
    }

    @Test
    void nextAndPreviousWrapAround() {
        Playlist playlist = new Playlist(FIRST_INDEX);
        playlist.addAll(files("a.jpg", "b.jpg"));

        assertEquals("b.jpg", nameOf(playlist.next()));
        assertEquals("a.jpg", nameOf(playlist.next()));
        assertEquals("b.jpg", nameOf(playlist.previous()));
    }

    @Test
    void entryReportsPositionAndSize() {
        Playlist playlist = new Playlist(FIRST_INDEX);
        playlist.addAll(files("a.jpg", "b.jpg", "c.jpg"));

        PlaylistEntry entry = playlist.next().orElseThrow();
        assertEquals(1, entry.position());
        assertEquals(3, entry.size());
    }

    @Test
    void removingCurrentMovesToFollowingFile() {
        Playlist playlist = new Playlist(FIRST_INDEX);
        playlist.addAll(files("a.jpg", "b.jpg", "c.jpg"));
        playlist.next();

        assertEquals("c.jpg", nameOf(playlist.remove(playlist.current().orElseThrow().mediaFile())));
    }

    @Test
    void removingLastFileWrapsToStart() {
        Playlist playlist = new Playlist(FIRST_INDEX);
        playlist.addAll(files("a.jpg", "b.jpg"));
        playlist.next();

        assertEquals("a.jpg", nameOf(playlist.remove(playlist.current().orElseThrow().mediaFile())));
    }

    @Test
    void removingOnlyFileEmptiesPlaylist() {
        Playlist playlist = new Playlist(FIRST_INDEX);
        playlist.addAll(files("a.jpg"));

        assertTrue(playlist.remove(playlist.current().orElseThrow().mediaFile()).isEmpty());
    }

    @Test
    void awaitCurrentReturnsOnceFilesArrive() throws Exception {
        Playlist playlist = new Playlist(FIRST_INDEX);
        CompletableFuture<PlaylistEntry> waiting = CompletableFuture.supplyAsync(() -> {
            try {
                return playlist.awaitCurrent();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        playlist.addAll(files("a.jpg"));
        assertEquals("a.jpg", waiting.get(5, TimeUnit.SECONDS).file().getName());
    }
}
