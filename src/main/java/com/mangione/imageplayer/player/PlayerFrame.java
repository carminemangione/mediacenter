package com.mangione.imageplayer.player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The always-on-top window. Clicking it toggles the title bar.
 */
public class PlayerFrame {
    private static final Dimension INITIAL_SIZE = new Dimension(300, 400);

    private final JFrame frame = new JFrame();
    private float transparency = 1.0f;

    public PlayerFrame(JComponent content) {
        frame.setLayout(new BorderLayout());
        frame.add(content, BorderLayout.CENTER);
        frame.setBackground(Color.lightGray);
        frame.setPreferredSize(INITIAL_SIZE);
        frame.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(Math.max(0, screenSize.width - INITIAL_SIZE.width), 0);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        applyTransparency();
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleDecoration();
            }
        });
    }

    public void addKeyListener(KeyListener listener) {
        frame.addKeyListener(listener);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void adjustTransparency(float delta) {
        transparency = Math.max(0f, Math.min(1f, transparency + delta));
        applyTransparency();
    }

    private void applyTransparency() {
        frame.getRootPane().putClientProperty("Window.alpha", transparency);
    }

    private void toggleDecoration() {
        // Decoration can only change while the frame is not displayable
        frame.dispose();
        frame.setUndecorated(!frame.isUndecorated());
        frame.setVisible(true);
    }
}
