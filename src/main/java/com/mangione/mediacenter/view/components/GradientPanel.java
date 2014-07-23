package com.mangione.mediacenter.view.components;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    protected void addGradient(Graphics2D graphics2d, int bottomOfGradient, int heightToMask, int widthToMask,
            boolean down, Color gradientColor) {
        Color oldColor = graphics2d.getColor();
        graphics2d.setColor(gradientColor);
        double startingAlphaComposite = 1;
        int gradientStart = bottomOfGradient;
        for (int i = 0; i < heightToMask; i++) {
            double alphaComposite = startingAlphaComposite - ((float) i / (float) heightToMask);
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)

                    alphaComposite);
            graphics2d.setComposite(ac);
            final int direction = down ? 1 : -1;
            graphics2d.fillRect(0, gradientStart, widthToMask, 1);
            gradientStart += direction;
        }
        graphics2d.setColor(oldColor);
    }
}
