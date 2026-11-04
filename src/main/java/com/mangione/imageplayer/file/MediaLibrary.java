package com.mangione.imageplayer.file;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Finds media files under a root directory and feeds them to a playlist, using the cache to skip known directories.
 */
public class MediaLibrary {
    private static final List<String> EXCLUDED_NAME_FRAGMENTS = List.of("WICThumbs", "DFTTH");

    private final File rootDirectory;
    private final MediaCache cache;
    private final Playlist playlist;
    private final Predicate<File> isMedia;

    public MediaLibrary(File rootDirectory, MediaCache cache, Playlist playlist, Predicate<File> isSupported) {
        this.rootDirectory = rootDirectory;
        this.cache = cache;
        this.playlist = playlist;
        this.isMedia = isSupported.and(file -> EXCLUDED_NAME_FRAGMENTS.stream().noneMatch(file.getName()::contains));
    }

    public void loadInBackground() {
        loadInBackground(() -> {
        });
    }

    /**
     * Starts loading on a daemon thread and calls onFinished on that thread once loading is done.
     */
    public void loadInBackground(Runnable onFinished) {
        Thread thread = new Thread(() -> {
            load();
            onFinished.run();
        }, "MediaLibraryLoader");
        thread.setDaemon(true);
        thread.start();
    }

    private void load() {
        if (!rootDirectory.isDirectory()) {
            System.err.printf("Not a directory: %s%n", rootDirectory.getAbsolutePath());
            return;
        }
        // A directory the OS won't let us list (e.g. a network volume without permission) looks empty, not missing
        if (rootDirectory.listFiles() == null) {
            System.err.printf("Can't read: %s%n", rootDirectory.getAbsolutePath());
            return;
        }
        long start = System.currentTimeMillis();
        MediaCache.Snapshot cached = cache.load();
        System.out.printf("Read cache: %d files, %d directories in %d ms%n", cached.filePaths().size(),
                cached.directories().size(), System.currentTimeMillis() - start);
        Set<String> knownFiles = new HashSet<>(cached.filePaths());
        Set<String> loadedDirectories = new HashSet<>(cached.directories());

        // Trust the cache rather than stat each path: on a network volume that takes hours, and the player
        // drops a file that has gone missing when it fails to load
        playlist.addAll(cached.filePaths().stream()
                .map(File::new)
                .filter(isMedia)
                .map(MediaFile::new)
                .toList());

        // Files directly in the root are always rechecked; subdirectories are skipped once loaded
        addNewFilesIn(rootDirectory, knownFiles);
        subdirectoriesOf(rootDirectory)
                .filter(directory -> !loadedDirectories.contains(directory.getPath()))
                .forEach(directory -> scan(directory, loadedDirectories, knownFiles));

        System.out.println("Saving cache");
        cache.save(new MediaCache.Snapshot(loadedDirectories, playlist.paths()));
        System.out.println("Loading thread finished");
    }

    private void scan(File directory, Set<String> loadedDirectories, Set<String> knownFiles) {
        if (loadedDirectories.add(directory.getPath())) {
            System.out.printf("Loading directory %s%n", directory.getAbsolutePath());
            addNewFilesIn(directory, knownFiles);
        }
        subdirectoriesOf(directory).forEach(subdirectory -> scan(subdirectory, loadedDirectories, knownFiles));
    }

    private void addNewFilesIn(File directory, Set<String> knownFiles) {
        playlist.addAll(filesIn(directory)
                .filter(File::isFile)
                .filter(isMedia)
                .filter(file -> !knownFiles.contains(file.getAbsolutePath()))
                .map(MediaFile::new)
                .sorted()
                .toList());
    }

    private static Stream<File> subdirectoriesOf(File directory) {
        return filesIn(directory).filter(File::isDirectory);
    }

    private static Stream<File> filesIn(File directory) {
        File[] files = directory.listFiles();
        return files == null ? Stream.empty() : Arrays.stream(files);
    }
}
