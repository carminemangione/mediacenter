package com.mangione.mediacenter.view.panels;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImagePanel extends JPanel {
    private final ImageIcon imageIcon;

    public ImagePanel(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public ImagePanel(ImageIcon imageIcon, int width, int height) {
        this.imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public ImagePanel(URL posterUrl) {
        this(new ImageIcon(posterUrl));
    }

    public ImagePanel(URL posterUrl, int width, int height) {
        this(new ImageIcon(posterUrl), width, height);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight());
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        imageIcon.paintIcon(this, graphics, 0, 0);
    }
}
