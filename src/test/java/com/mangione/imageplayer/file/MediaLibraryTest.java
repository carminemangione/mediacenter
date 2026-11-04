package com.mangione.imageplayer.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pins the loader's design for a large network volume: trust the cache, skip directories already scanned,
 * and only touch the disk for what is new.
 */
class MediaLibraryTest {
    @TempDir
    File root;

    private MediaCache cache;
    private Playlist playlist;

    @BeforeEach
    void setUp() {
        cache = new MediaCache(root);
        playlist = new Playlist(new Random(0));
    }

    private void loadLibrary() throws InterruptedException {
        CountDownLatch finished = new CountDownLatch(1);
        new MediaLibrary(root, cache, playlist, file -> file.getName().endsWith(".jpg"))
                .loadInBackground(finished::countDown);
        assertTrue(finished.await(5, TimeUnit.SECONDS), "library did not finish loading");
    }

    private static File create(File parent, String path) throws IOException {
        File file = new File(parent, path);
        Files.createDirectories(file.getParentFile().toPath());
        Files.createFile(file.toPath());
        return file;
    }

    private static File directory(File parent, String name) throws IOException {
        return Files.createDirectories(new File(parent, name).toPath()).toFile();
    }

    @Test
    void cachedFilesArePlayedWithoutCheckingThatTheyStillExistOnDisk() throws Exception {
        // Checking each of ~568k cached paths on the network volume turns a seconds-long startup into hours
        String missing = new File(root, "gone/missing.jpg").getAbsolutePath();
        cache.save(new MediaCache.Snapshot(Set.of(), List.of(missing)));

        loadLibrary();

        assertTrue(playlist.paths().contains(missing));
    }

    @Test
    void topLevelDirectoriesAlreadyInTheCacheAreNotRescanned() throws Exception {
        File loaded = directory(root, "loaded");
        create(loaded, "new.jpg");
        create(loaded, "nested/new.jpg");
        cache.save(new MediaCache.Snapshot(Set.of(loaded.getPath()), List.of()));

        loadLibrary();

        assertTrue(playlist.paths().isEmpty());
    }

    @Test
    void newTopLevelDirectoriesAreScannedIncludingTheirSubdirectories() throws Exception {
        File loaded = directory(root, "loaded");
        cache.save(new MediaCache.Snapshot(Set.of(loaded.getPath()), List.of()));
        File top = create(root, "fresh/top.jpg");
        File nested = create(root, "fresh/nested/deep.jpg");

        loadLibrary();

        assertEquals(Set.of(top.getAbsolutePath(), nested.getAbsolutePath()), Set.copyOf(playlist.paths()));
    }

    @Test
    void filesDirectlyInTheRootAreAlwaysPickedUpEvenWithACache() throws Exception {
        cache.save(new MediaCache.Snapshot(Set.of(), List.of()));
        File inRoot = create(root, "root.jpg");

        loadLibrary();

        assertEquals(List.of(inRoot.getAbsolutePath()), playlist.paths());
    }

    @Test
    void cachedFilesAreNotAddedTwiceWhenTheirDirectoryIsScanned() throws Exception {
        File known = create(root, "known.jpg");
        cache.save(new MediaCache.Snapshot(Set.of(), List.of(known.getAbsolutePath())));

        loadLibrary();

        assertEquals(List.of(known.getAbsolutePath()), playlist.paths());
    }

    @Test
    void scannedDirectoriesAndFoundFilesAreSavedSoTheNextStartupSkipsThem() throws Exception {
        File found = create(root, "fresh/found.jpg");

        loadLibrary();

        MediaCache.Snapshot saved = cache.load();
        assertTrue(saved.directories().contains(new File(root, "fresh").getPath()));
        assertEquals(List.of(found.getAbsolutePath()), saved.filePaths());
    }
}
