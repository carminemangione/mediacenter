package com.mangione.imageplayer.player;

import javax.swing.*;
import java.awt.*;

public class ImagePlayerPanel extends JFrame {
    private float transparency;

    public ImagePlayerPanel(JComponent content, boolean undecorated, Dimension size, Point location, float transparency) {
        this.transparency = transparency;
        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);
        setUndecorated(undecorated);
        setBackground(Color.lightGray);
        setPreferredSize(size);
        pack();
        setLocation(location.x, location.y);
        getRootPane().putClientProperty("Window.alpha", transparency);
        setVisible(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    void adjustTransparency(float delta) {
        transparency = Math.max(0f, Math.min(1f, transparency + delta));
        getRootPane().putClientProperty("Window.alpha", transparency);
    }

    float getTransparency() {
        return transparency;
    }
}
