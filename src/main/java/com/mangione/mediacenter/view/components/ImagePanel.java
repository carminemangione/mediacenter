package com.mangione.mediacenter.view.components;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private final ImageIcon imageIcon;

    public ImagePanel(ImageIcon imageIcon, int width, int height) {
        this.imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
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
