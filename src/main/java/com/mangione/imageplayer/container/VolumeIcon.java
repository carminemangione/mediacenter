package com.mangione.imageplayer.container;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;

class VolumeIcon implements Icon {
    private static final int SIZE = 22;
    private static final double DESIGN_SIZE = 14.0;
    private final boolean muted;

    VolumeIcon(boolean muted) {
        this.muted = muted;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(x, y);
        g2.scale(SIZE / DESIGN_SIZE, SIZE / DESIGN_SIZE);
        g2.setColor(muted ? Color.GRAY : Color.DARK_GRAY);

        Path2D speaker = new Path2D.Double();
        speaker.moveTo(1, 5);
        speaker.lineTo(4, 5);
        speaker.lineTo(8, 1);
        speaker.lineTo(8, 13);
        speaker.lineTo(4, 9);
        speaker.lineTo(1, 9);
        speaker.closePath();
        g2.fill(speaker);

        g2.setStroke(new BasicStroke(1.3f));
        if (muted) {
            g2.drawLine(10, 4, 13, 10);
            g2.drawLine(13, 4, 10, 10);
        } else {
            g2.draw(new Arc2D.Double(6, 4, 5, 6, -60, 120, Arc2D.OPEN));
            g2.draw(new Arc2D.Double(5, 1, 8, 12, -60, 120, Arc2D.OPEN));
        }
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return SIZE;
    }

    @Override
    public int getIconHeight() {
        return SIZE;
    }
}
