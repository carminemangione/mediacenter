package com.mangione.mediacenter.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private final BufferedImage image;
    private final Dimension dimension;

    public ImagePanel(BufferedImage image) {
        this.image = image;
        dimension = new Dimension(image.getWidth(), image.getHeight());
    }

    @Override
    public Dimension getPreferredSize() {
        return dimension;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int height = getHeight();
        int width = getWidth();
        int xStart = (width - dimension.width) / 2;
        int yStart = (height - dimension.height) / 2;

        g.drawImage(image, xStart, yStart, null);
    }
}
