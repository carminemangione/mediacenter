package com.mangione.imageplayer.swingimage;

import javax.swing.*;
import java.awt.*;

class SwingImagePanel extends JPanel {
    private final JLabel imageLabel;

    SwingImagePanel() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(Color.DARK_GRAY);
        imageLabel = new JLabel();
        imageLabel.setOpaque(false);
        add(imageLabel, BorderLayout.CENTER);
    }

    void setImage(ImageIcon icon) {
        imageLabel.setIcon(icon);
        repaint();
    }
}
