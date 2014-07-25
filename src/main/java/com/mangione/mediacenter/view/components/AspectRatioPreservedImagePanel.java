package com.mangione.mediacenter.view.components;

import com.mangione.mediacenter.model.AspectRatioBox;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AspectRatioPreservedImagePanel extends JPanel {
    private final BufferedImage image;
    private final Dimension imageSize;

    public AspectRatioPreservedImagePanel(BufferedImage imageIcon) {
        this.image = imageIcon;
        imageSize = new Dimension(image.getWidth(), image.getHeight());
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        Dimension panelSize = getSize();


        double imageWidth = imageSize.getWidth();
        double imageHeight = imageSize.getHeight();
        final AspectRatioBox adjustedImageDimension = new AspectRatioBox(imageWidth, imageHeight)
                .fitHeightThenWidth(panelSize.getWidth(), panelSize.getHeight());

        int imageStartX = (int) ((panelSize.getWidth() - adjustedImageDimension.getWidth())) / 2;
        int imageStartY = (int) ((panelSize.getHeight() - adjustedImageDimension.getHeight())) / 2;
        g2d.drawImage(image,
                imageStartX, imageStartY,
                (int) adjustedImageDimension.getWidth(), (int) adjustedImageDimension.getHeight(), null);

    }
}
