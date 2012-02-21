package com.mangione.mediacenter.view.rottentomatoes;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImagePanel extends JPanel {
    private final ImageIcon imageIcon;

    public ImagePanel(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public ImagePanel(URL posterUrl) {
        this(new ImageIcon(posterUrl));
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
