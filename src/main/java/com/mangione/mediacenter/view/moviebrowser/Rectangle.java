package com.mangione.mediacenter.view.moviebrowser;

import java.awt.*;

public class Rectangle {
    private final Point topLeft;
    private final Point bottomRight;

    public Rectangle(int minX, int minY, int maxX, int maxY) {
        topLeft = new Point(minX, minY);
        bottomRight = new Point(maxX, maxY);
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "topLeft=" + topLeft +
                ", bottomRight=" + bottomRight +
                '}';
    }
}
