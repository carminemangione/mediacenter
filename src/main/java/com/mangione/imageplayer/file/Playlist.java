package com.mangione.imageplayer.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Thread-safe list of media files with a cursor. Files may be added while it is being played.
 */
public class Playlist {
    private final List<MediaFile> files = new ArrayList<>();
    private final Random random;
    private int index = -1;

    public Playlist(Random random) {
        this.random = random;
    }

    public synchronized void addAll(Collection<MediaFile> added) {
        if (added.isEmpty()) return;
        files.addAll(added);
        if (index < 0) {
            index = random.nextInt(files.size());
        }
        notifyAll();
    }

    public synchronized PlaylistEntry awaitCurrent() throws InterruptedException {
        while (files.isEmpty()) {
            wait();
        }
        return entryAtIndex();
    }

    public synchronized Optional<PlaylistEntry> current() {
        return files.isEmpty() ? Optional.empty() : Optional.of(entryAtIndex());
    }

    public synchronized Optional<PlaylistEntry> next() {
        if (files.isEmpty()) return Optional.empty();
        index = (index + 1) % files.size();
        return current();
    }

    public synchronized Optional<PlaylistEntry> previous() {
        if (files.isEmpty()) return Optional.empty();
        index = (index - 1 + files.size()) % files.size();
        return current();
    }

    public synchronized Optional<PlaylistEntry> jumpToRandom() {
        if (files.isEmpty()) return Optional.empty();
        index = random.nextInt(files.size());
        return current();
    }

    /**
     * Removes the file and returns the entry that now occupies the cursor.
     */
    public synchronized Optional<PlaylistEntry> remove(MediaFile file) {
        int removed = files.indexOf(file);
        if (removed >= 0) {
            files.remove(removed);
            if (files.isEmpty()) {
                index = -1;
            } else {
                if (removed < index) index--;
                index %= files.size();
            }
        }
        return current();
    }

    public synchronized List<String> paths() {
        return files.stream()
                .map(mediaFile -> mediaFile.getFile().getAbsolutePath())
                .toList();
    }

    private PlaylistEntry entryAtIndex() {
        return new PlaylistEntry(files.get(index), index, files.size());
    }
}
