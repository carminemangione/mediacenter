package com.mangione.imageplayer;

import com.mangione.imageplayer.container.MediaContainerPanel;
import com.mangione.imageplayer.file.MediaCache;
import com.mangione.imageplayer.file.MediaLibrary;
import com.mangione.imageplayer.file.Playlist;
import com.mangione.imageplayer.media.MediaTypeRegistry;
import com.mangione.imageplayer.media.image.ImageMedia;
import com.mangione.imageplayer.media.video.VideoMedia;
import com.mangione.imageplayer.player.ImagePlayerController;
import com.mangione.imageplayer.player.PlaybackEngine;
import com.mangione.imageplayer.player.PlayerFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main {
    private static final float TRANSPARENCY_STEP = 0.02f;

    public static void main(String[] args) throws Exception {
        File rootDirectory = args.length > 0 ? new File(args[0]) : new File("/Volumes/Pictures");

        MediaTypeRegistry mediaTypes = new MediaTypeRegistry()
                .register(ImageMedia::still, "jpg", "jpeg", "png", "webp")
                .register(ImageMedia::animated, "gif")
                .register(VideoMedia::new, "mp4");
        Playlist playlist = new Playlist(new Random());
        MediaLibrary library = new MediaLibrary(rootDirectory, new MediaCache(rootDirectory), playlist,
                mediaTypes::isSupported);
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "MediaWorker");
            thread.setDaemon(true);
            return thread;
        });

        SwingUtilities.invokeAndWait(() -> {
            MediaContainerPanel container = new MediaContainerPanel();
            ImagePlayerController controller = new ImagePlayerController(playlist, mediaTypes, container,
                    new PlaybackEngine(worker), worker);
            PlayerFrame frame = new PlayerFrame(container);
            frame.addKeyListener(controller.keyBindings()
                    .bind(KeyEvent.VK_T, () -> frame.adjustTransparency(-TRANSPARENCY_STEP))
                    .bind(KeyEvent.VK_Y, () -> frame.adjustTransparency(TRANSPARENCY_STEP)));
            frame.show();
            controller.start();
        });
        library.loadInBackground();
    }
}
