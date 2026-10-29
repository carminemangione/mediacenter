package com.mangione.imageplayer.player;

public class PlaybackEngine {
    private static final int DEFAULT_DURATION_MILLIS = 3000;
    private static final int SPEED_INCREMENT_MILLIS = 500;
    private static final int SPEED_DECREMENT_MILLIS = 5000;
    private static final int MIN_DURATION_MILLIS = 5000;

    private final PlaybackListener listener;
    private volatile int durationMillis = DEFAULT_DURATION_MILLIS;
    private volatile boolean paused = false;
    private volatile boolean running = false;
    private Thread thread;
    private long lastSwitchTimeMillis;

    PlaybackEngine(PlaybackListener listener) {
        this.listener = listener;
    }

    void start() {
        running = true;
        lastSwitchTimeMillis = System.currentTimeMillis();
        thread = new Thread(this::loop, "PlaybackEngine");
        thread.setDaemon(true);
        thread.start();
    }

    synchronized void pause() {
        paused = true;
    }

    synchronized void resume() {
        paused = false;
        notifyAll();
    }

    void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    void setDuration(int millis) {
        durationMillis = millis;
        lastSwitchTimeMillis = System.currentTimeMillis();
    }

    void increaseSpeed() {
        durationMillis += SPEED_INCREMENT_MILLIS;
    }

    void decreaseSpeed() {
        durationMillis = Math.max(durationMillis - SPEED_DECREMENT_MILLIS, MIN_DURATION_MILLIS);
    }

    private void loop() {
        while (running) {
            synchronized (this) {
                while (paused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            long elapsed = System.currentTimeMillis() - lastSwitchTimeMillis;
            if (elapsed >= durationMillis) {
                lastSwitchTimeMillis = System.currentTimeMillis();
                listener.onTimerExpired();
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
