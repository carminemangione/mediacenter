package com.mangione.imageplayer.container;

import com.mangione.imageplayer.player.MediaDisplay;
import com.mangione.imageplayer.media.PlayableMedia;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MediaContainerPanel extends JPanel implements MediaDisplay {
    private static final Icon MUTED_ICON = new VolumeIcon(true);
    private static final Icon UNMUTED_ICON = new VolumeIcon(false);

    private final JLabel statusLabel;
    private final JLabel loadingLabel;
    private final JLabel volumeLabel;
    private final JPanel mediaSlot;
    private PlayableMedia currentMedia;
    private boolean muted = true;

    public MediaContainerPanel() {
        setLayout(new BorderLayout());

        mediaSlot = new JPanel(new BorderLayout());
        mediaSlot.setBackground(Color.DARK_GRAY);

        statusLabel = new JLabel("<no media>", JLabel.CENTER);
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 9.0f));

        loadingLabel = new JLabel("Loading library…");
        loadingLabel.setForeground(Color.DARK_GRAY);
        loadingLabel.setFont(statusLabel.getFont());

        // A JLabel rather than a JButton so clicking it doesn't steal keyboard focus from the frame
        volumeLabel = new JLabel(MUTED_ICON);
        volumeLabel.setToolTipText("Click to toggle sound");
        volumeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        volumeLabel.setVisible(false);
        volumeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleMute();
            }
        });

        JPanel statusBar = new JPanel(new BorderLayout(4, 0));
        statusBar.setBorder(new EmptyBorder(4, 4, 4, 4));
        statusBar.add(loadingLabel, BorderLayout.WEST);
        statusBar.add(statusLabel, BorderLayout.CENTER);
        statusBar.add(volumeLabel, BorderLayout.EAST);

        add(mediaSlot, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    @Override
    public void display(PlayableMedia media, String status) {
        SwingUtilities.invokeLater(() -> {
            if (currentMedia != null) {
                currentMedia.dispose();
            }
            currentMedia = media;
            muted = true;
            volumeLabel.setIcon(MUTED_ICON);
            volumeLabel.setVisible(false);
            // A video only knows whether it has sound once it is ready, which may be after it is shown
            media.ready().thenRun(() -> SwingUtilities.invokeLater(() -> {
                if (currentMedia == media) volumeLabel.setVisible(media.hasAudio());
            }));

            statusLabel.setText(status);
            mediaSlot.removeAll();
            mediaSlot.add(media.getComponent(), BorderLayout.CENTER);
            mediaSlot.revalidate();
            mediaSlot.repaint();
        });
    }

    /**
     * Hides the loading status; safe to call from any thread.
     */
    public void libraryLoaded() {
        SwingUtilities.invokeLater(() -> loadingLabel.setVisible(false));
    }

    private void toggleMute() {
        if (currentMedia == null) return;
        muted = !muted;
        currentMedia.setMuted(muted);
        volumeLabel.setIcon(muted ? MUTED_ICON : UNMUTED_ICON);
    }
}
