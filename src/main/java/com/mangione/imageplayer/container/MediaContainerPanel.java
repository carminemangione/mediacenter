package com.mangione.imageplayer.container;

import com.mangione.imageplayer.player.PlayerPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MediaContainerPanel extends JPanel {
    private static final Icon MUTED_ICON = new VolumeIcon(true);
    private static final Icon UNMUTED_ICON = new VolumeIcon(false);

    private final JLabel statusLabel;
    private final JLabel volumeLabel;
    private final JPanel mediaSlot;
    private PlayerPanel currentPlayerPanel;
    private boolean muted = true;

    public MediaContainerPanel() {
        setLayout(new BorderLayout());

        mediaSlot = new JPanel(new BorderLayout());
        mediaSlot.setBackground(Color.DARK_GRAY);

        statusLabel = new JLabel("<no media>", JLabel.CENTER);
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setFont(statusLabel.getFont().deriveFont(Font.ITALIC, 9.0f));

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
        statusBar.add(statusLabel, BorderLayout.CENTER);
        statusBar.add(volumeLabel, BorderLayout.EAST);

        add(mediaSlot, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    public void display(PlayerPanel playerPanel, String status) {
        SwingUtilities.invokeLater(() -> {
            if (currentPlayerPanel != null) {
                currentPlayerPanel.dispose();
            }
            currentPlayerPanel = playerPanel;
            muted = true;
            volumeLabel.setIcon(MUTED_ICON);
            volumeLabel.setVisible(playerPanel.hasAudio());

            statusLabel.setText(status);
            mediaSlot.removeAll();
            mediaSlot.add(playerPanel.getPanel(), BorderLayout.CENTER);
            mediaSlot.revalidate();
            mediaSlot.repaint();
        });
    }

    private void toggleMute() {
        if (currentPlayerPanel == null) return;
        muted = !muted;
        currentPlayerPanel.setMuted(muted);
        volumeLabel.setIcon(muted ? MUTED_ICON : UNMUTED_ICON);
    }
}
