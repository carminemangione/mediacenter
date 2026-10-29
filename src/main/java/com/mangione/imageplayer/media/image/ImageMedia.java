package com.mangione.imageplayer.media.image;

import com.mangione.imageplayer.media.PlayableMedia;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

public class ImageMedia implements PlayableMedia {
    private final ImageIcon rawImage;
    private SwingImagePanel panel;

    public static ImageMedia still(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        if (image == null) {
            throw new IOException("No image reader for " + file);
        }
        return new ImageMedia(new ImageIcon(image));
    }

    // ImageIcon keeps the animation frames that ImageIO would flatten to the first frame.
    // createImage rather than ImageIcon(path), which caches every image in the Toolkit forever.
    public static ImageMedia animated(File file) {
        return new ImageMedia(new ImageIcon(Toolkit.getDefaultToolkit().createImage(file.getPath())));
    }

    private ImageMedia(ImageIcon rawImage) {
        this.rawImage = rawImage;
    }

    private SwingImagePanel createPanel() {
        SwingImagePanel created = new SwingImagePanel();
        created.setImage(rawImage);
        created.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    ImageIcon scaled = scaleToPanel(rawImage);
                    if (scaled != null) created.setImage(scaled);
                });
            }
        });
        return created;
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
    public JComponent getComponent() {
        if (panel == null) {
            panel = createPanel();
        }
        return panel;
    }

    @Override
    public Optional<Duration> getNaturalDuration() {
        return Optional.empty();
    }

    @Override
    public boolean hasAudio() {
        return false;
    }

    @Override
    public void setMuted(boolean muted) {
    }

    @Override
    public void dispose() {
        rawImage.getImage().flush();
    }
}
