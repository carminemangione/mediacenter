package com.mangione.imageplayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class ImagePanel extends JPanel {
    private Dimension imageSize;
    private Image currentImage;
    private String currentImageFile;


    public ImagePanel() {
    }

    public synchronized void setImage(final String imageFileName) {
        currentImageFile = imageFileName;
        if (currentImage != null) {
            currentImage = null;
        }

        SwingUtilities.invokeLater(() -> {
            invalidate();
            repaint();
        });
//        new Thread() {
//            @Override
//            public void run() {
//                if (currentImageFile == null) {
//                    currentImageFile = imageFileName;
//                }
//                numberOfSteps = 0;
//                fadeOut = true;
//                animating = true;
//                currentAlpha = 1.0f;
//                while (animating) {
//                    try {
//                        if (!fadeOut) {
//                            if (numberOfSteps == NUMBER_OF_STEPS) {
//                                currentAlpha = 1.0f;
//                                animating = false;
//                            } else {
//                                currentAlpha += 0.95f / NUMBER_OF_STEPS;
//                                numberOfSteps++;
//                            }
//
//                        } else {
//                            if (numberOfSteps == NUMBER_OF_STEPS) {
//                                fadeOut = false;
//                                numberOfSteps = 0;
//                                currentAlpha = 0.0f;
//                                invalidate();
//                                repaint();
//                                Thread.sleep(500);
//                                currentImageFile = imageFileName;
//                                currentImage = null;
//                            } else {
//                                numberOfSteps++;
//                                currentAlpha -= 0.75f / NUMBER_OF_STEPS;
//                            }
//
//                        }
//
//                        currentAlpha = Math.min(1.0f, currentAlpha);
//                        currentAlpha = Math.max(0f, currentAlpha);
//                        SwingUtilities.invokeLater(new Runnable() {
//                            public void run() {
//                                invalidate();
//                                repaint();
//                            }
//                        });
//                        Thread.sleep(50);
//                    } catch (InterruptedException e) {
//                        // noop
//                    }
//                }

//            }
//        }.start();


    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension panelSize = getSize();
        if (currentImage == null) {
            try {
                currentImage = ImageIO.read(new File(currentImageFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

        int imageStartX = (int) ((panelWidth - adjustedImageWidth)) / 2;
        int imageStartY = (int) ((panelHeight - adjustedImageHeight)) / 2;
        super.paint(g);
        g2d.setColor(Color.darkGray);
        g2d.fillRect(0, 0, panelSize.width, panelSize.height);
        g2d.drawImage(currentImage, imageStartX, imageStartY, (int) adjustedImageWidth, (int) adjustedImageHeight, null);

    }

    public Dimension getMaximumSize() {
        return imageSize;
    }

}