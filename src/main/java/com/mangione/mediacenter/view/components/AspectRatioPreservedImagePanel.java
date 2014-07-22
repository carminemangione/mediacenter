package com.mangione.mediacenter.view.components;

import com.mangione.mediacenter.model.AspectRatioBox;

import javax.swing.*;
import java.awt.*;

public class AspectRatioPreservedImagePanel extends JPanel {
    private final ImageIcon image;

    public AspectRatioPreservedImagePanel(ImageIcon imageIcon) {
        this.image = imageIcon;
    }


    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D g2d = (Graphics2D) graphics;
        Dimension panelSize = getSize();


        double imageWidth = image.getIconWidth();
        double imageHeight = image.getIconHeight();
        final AspectRatioBox adjustedImageDimension = new AspectRatioBox(imageWidth, imageHeight)
                .fitHeightThenWidth(panelSize.getWidth(), panelSize.getHeight());

        int imageStartX = (int) ((panelSize.getWidth() - adjustedImageDimension.getWidth())) / 2;
        int imageStartY = (int) ((panelSize.getHeight() - adjustedImageDimension.getHeight())) / 2;
        g2d.drawImage(image.getImage(),
                imageStartX, imageStartY,
                (int) adjustedImageDimension.getWidth(), (int) adjustedImageDimension.getHeight(), null);

    }
}
