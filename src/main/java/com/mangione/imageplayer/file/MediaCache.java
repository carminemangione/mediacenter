package com.mangione.imageplayer.file;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Remembers which directories were scanned and which files were found, so startup doesn't rescan everything.
 */
public class MediaCache {
    private static final String DIRECTORIES_FILE = "loadedDirectories.json";
    private static final String FILE_PATHS_FILE = "imageFilePaths.json";

    public record Snapshot(Set<String> directories, List<String> filePaths) {
        static Snapshot empty() {
            return new Snapshot(Set.of(), List.of());
        }
    }

    private final Gson gson = new Gson();
    private final Path directoriesPath;
    private final Path filePathsPath;

    public MediaCache(File rootDirectory) {
        directoriesPath = rootDirectory.toPath().resolve(DIRECTORIES_FILE);
        filePathsPath = rootDirectory.toPath().resolve(FILE_PATHS_FILE);
    }

    public Snapshot load() {
        if (!Files.exists(directoriesPath) || !Files.exists(filePathsPath)) {
            return Snapshot.empty();
        }
        try {
            Set<String> directories = gson.fromJson(Files.readString(directoriesPath), new TypeToken<Set<String>>() {
            }.getType());
            List<String> filePaths = gson.fromJson(Files.readString(filePathsPath), new TypeToken<List<String>>() {
            }.getType());
            return new Snapshot(Objects.requireNonNullElse(directories, Set.of()),
                    Objects.requireNonNullElse(filePaths, List.of()));
        } catch (IOException | JsonParseException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
            return Snapshot.empty();
        }
    }

    public void save(Snapshot snapshot) {
        try {
            write(directoriesPath, snapshot.directories());
            write(filePathsPath, snapshot.filePaths());
        } catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    private void write(Path path, Object value) throws IOException {
        Files.writeString(path, gson.toJson(value));
    }
}
