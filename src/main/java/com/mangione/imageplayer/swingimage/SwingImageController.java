package com.mangione.imageplayer.swingimage;

import com.mangione.imageplayer.player.PlayerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SwingImageController implements PlayerPanel {
    private static final int DURATION_MILLIS = 5000;
    private static final int LOAD_TIMEOUT_MILLIS = 5000;

    private final SwingImagePanel panel;
    private final ImageIcon rawImage;

    public SwingImageController(String filePath) {
        panel = new SwingImagePanel();
        rawImage = loadImage(filePath);
        panel.setImage(rawImage);

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    ImageIcon scaled = scaleToPanel(rawImage);
                    if (scaled != null) panel.setImage(scaled);
                });
            }
        });
    }

    private ImageIcon loadImage(String filePath) {
        ImageLoaderThread loader = new ImageLoaderThread(filePath);
        loader.start();
        try {
            loader.join(LOAD_TIMEOUT_MILLIS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (loader.isAlive()) {
            loader.interrupt();
        }
        return loader.getImageIcon();
    }

    private ImageIcon scaleToPanel(ImageIcon icon) {
        if (icon == null) return null;
        Dimension panelSize = panel.getSize();
        if (panelSize.width == 0 || panelSize.height == 0) return icon;

        double panelWidth = panelSize.width;
        double panelHeight = panelSize.height;
        double imageWidth = icon.getIconWidth() * 2.0;
        double imageHeight = icon.getIconHeight() * 2.0;
        double aspectRatio = imageWidth / imageHeight;

        double adjWidth, adjHeight;
        if (imageWidth <= panelWidth && imageHeight < panelHeight) {
            adjWidth = imageWidth;
            adjHeight = imageHeight;
        } else {
            adjWidth = Math.min(panelWidth, imageWidth * 2);
            adjHeight = panelWidth / aspectRatio;
            if (adjHeight >= panelHeight) {
                adjHeight = Math.min(panelHeight, imageHeight * 2);
                adjWidth = panelHeight * aspectRatio;
            }
        }

        return new ImageIcon(icon.getImage().getScaledInstance(
                (int) adjWidth, (int) adjHeight, Image.SCALE_REPLICATE));
    }

    @Override
    public JComponent getPanel() {
        return panel;
    }

    @Override
    public int getDurationMillis() {
        return DURATION_MILLIS;
    }
}
