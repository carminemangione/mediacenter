package com.mangione.imageplayer.player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class KeyBindings extends KeyAdapter {
    private final Map<Integer, Runnable> actions = new HashMap<>();

    public KeyBindings bind(int keyCode, Runnable action) {
        actions.put(keyCode, action);
        return this;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        Runnable action = actions.get(event.getKeyCode());
        if (action != null) {
            action.run();
        }
    }
}
