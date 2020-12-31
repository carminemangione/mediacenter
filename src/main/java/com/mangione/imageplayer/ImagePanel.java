package com.mangione.imageplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class ImagePanel extends JPanel {
    private Dimension imageSize;
    private String currentImageFile;
    private final JLabel jLabel;


    ImagePanel(String currentImageFile)  {
        setOpaque(true);
        setBackground(Color.DARK_GRAY);
        this.currentImageFile = currentImageFile;
        setLayout(new BorderLayout());

        jLabel = new JLabel(new ImageIcon(currentImageFile));
        jLabel.setOpaque(false);
        add(jLabel, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    jLabel.setIcon(getCurrentImage());
                    repaint();
                });
            }
        });

    }

    private ImageIcon getCurrentImage() {
        final ImageIcon imageIcon = new ImageIcon(currentImageFile);
        final QuadDimension quadDimension = new QuadDimension(imageIcon.getImage());
        return new ImageIcon(imageIcon.getImage().getScaledInstance(quadDimension.size.width,
               quadDimension.size.height, Image.SCALE_REPLICATE));
    }

    public synchronized void setImage(final String imageFileName) {
        currentImageFile = imageFileName;
        SwingUtilities.invokeLater(() -> {
            jLabel.setIcon(getCurrentImage());
            repaint();
        });

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    public Dimension getMaximumSize() {
        return imageSize;
    }

    private class QuadDimension {
        private final Dimension size;

        QuadDimension(Image currentImage) {
            Dimension panelSize = getSize();

            double panelWidth = panelSize.getWidth();
            double panelHeight = panelSize.getHeight();

            imageSize = new Dimension(currentImage.getWidth(null),
                    currentImage.getHeight(null));
            double imageWidth = currentImage.getWidth(null) * 2;
            double imageHeight = currentImage.getHeight(null) * 2;

            double adjustedImageWidth;
            double adjustedImageHeight;

            double aspectRatioOfImage = imageWidth / imageHeight;

            if (imageWidth <= panelWidth && imageHeight < panelHeight) {
                adjustedImageWidth = imageWidth;
                adjustedImageHeight = imageHeight;
            } else {
                adjustedImageWidth = Math.min(panelWidth, imageWidth * 2);
                adjustedImageHeight = panelWidth / aspectRatioOfImage;

                if (adjustedImageHeight >= panelHeight) {
                    adjustedImageHeight = Math.min(panelHeight, imageHeight * 2);
                    adjustedImageWidth = panelHeight * aspectRatioOfImage;
                }

            }
            size = new Dimension((int) adjustedImageWidth, (int) adjustedImageHeight);

        }

    }


}