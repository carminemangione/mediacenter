package com.mangione.mediacenter.model;

import com.google.common.base.Preconditions;

public class AspectRatioBox {

    private final double width;
    private final double height;

    public AspectRatioBox(double width, double height) {
        Preconditions.checkArgument(width >= 1, "width must be positive");
        Preconditions.checkArgument(height >= 1, "height must be positive");
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public AspectRatioBox fitHeightThenWidth(double maxWidth, double maxHeight) {


        double adjustedImageWidth;
        double adjustedImageHeight;

        double aspectRatioOfImage = width / height;

        final boolean imageFitsInPanel = width < maxWidth && height < maxHeight;
        if (imageFitsInPanel) {
            adjustedImageWidth = maxWidth;
            adjustedImageHeight = adjustedImageWidth / aspectRatioOfImage;
        } else {
            // Squeeze the image so that the width fits
            adjustedImageWidth = Math.min(maxWidth, width);
            adjustedImageHeight = maxWidth / aspectRatioOfImage;
        }

            final boolean heightStillDoesNotFit = adjustedImageHeight >= maxHeight;
            if (heightStillDoesNotFit) {
                // Squeeze the height so it fits
                adjustedImageHeight = maxHeight;
                adjustedImageWidth = maxHeight * aspectRatioOfImage;
            }

        return new AspectRatioBox((int) adjustedImageWidth, (int) adjustedImageHeight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AspectRatioBox)) return false;

        AspectRatioBox that = (AspectRatioBox) o;

        return height == that.height && width == that.width;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(width);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(height);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return width + "x" + height;
    }
}
