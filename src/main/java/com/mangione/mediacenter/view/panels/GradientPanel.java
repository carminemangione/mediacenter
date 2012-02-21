package com.mangione.mediacenter.view.panels;

import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    public GradientPanel(LayoutManager layoutManager, boolean b) {
        super(layoutManager, b);
    }

    public GradientPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    public GradientPanel(boolean b) {
        super(b);
    }

    public GradientPanel() {
        super();
    }

    protected void addGradient(Graphics2D graphics2d, int currentBottomOfGradient, int heightToMask, boolean down,
            Color gradientColor) {
        graphics2d.setColor(gradientColor);
        double startingAlphaComposite = 1;
        for (int i = 0; i < heightToMask; i++) {
            double alphaComposite = startingAlphaComposite - ((float) i / (float) heightToMask);
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)
                    alphaComposite);
            graphics2d.setComposite(ac);
            graphics2d.setColor(Color.black);
            graphics2d.fillRect(0, currentBottomOfGradient, getPreferredSize().width, 1);
            currentBottomOfGradient += (down ? 1 : -1);
        }
    }
}
