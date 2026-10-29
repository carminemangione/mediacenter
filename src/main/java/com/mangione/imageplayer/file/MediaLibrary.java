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
        Thread thread = new Thread(this::load, "MediaLibraryLoader");
        thread.setDaemon(true);
        thread.start();
    }

    void load() {
        if (!rootDirectory.isDirectory()) {
            System.err.printf("Not a directory: %s%n", rootDirectory.getAbsolutePath());
            return;
        }
        MediaCache.Snapshot cached = cache.load();
        Set<String> knownFiles = new HashSet<>(cached.filePaths());
        Set<String> loadedDirectories = new HashSet<>(cached.directories());

        playlist.addAll(cached.filePaths().stream()
                .map(File::new)
                .filter(File::isFile)
                .filter(isMedia)
                .map(MediaFile::new)
                .toList());

        // Files directly in the root are always rechecked; subdirectories are skipped once loaded
        addNewFilesIn(rootDirectory, knownFiles);
        subdirectoriesOf(rootDirectory).forEach(directory -> scan(directory, loadedDirectories, knownFiles));

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
