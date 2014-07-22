package com.mangione.mediacenter.view.components;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {

    public GradientPanel() {
        super();
    }

    protected void addGradient(Graphics2D graphics2d, int currentBottomOfGradient, int heightToMask,
            boolean down, Color gradientColor) {
        Color oldColor = graphics2d.getColor();
        graphics2d.setColor(gradientColor);
        double startingAlphaComposite = 1;
        for (int i = 0; i < heightToMask; i++) {
            double alphaComposite = startingAlphaComposite - ((float) i / (float) heightToMask);
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)
                    alphaComposite);
            graphics2d.setComposite(ac);
            graphics2d.fillRect(0, currentBottomOfGradient, getPreferredSize().width, 1);
            currentBottomOfGradient += (down ? 1 : -1);
        }
        graphics2d.setColor(oldColor);
    }
}
