package com.mangione.mediacenter.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AspectRatioBoxTest {

    @Test(expected = IllegalArgumentException.class)
    public void nonPositiveWidth() throws Exception {
        new AspectRatioBox(0, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nonPositiveHeight() throws Exception {
        new AspectRatioBox(5, 0);
    }

    @Test
    public void fitHeightThenWidthPreservedDimension() throws Exception {
        final AspectRatioBox aspectRatioBox = new AspectRatioBox(1600, 900);
        final AspectRatioBox perfectFit = aspectRatioBox.fitHeightThenWidth(1600, 900);
        assertEquals(aspectRatioBox, perfectFit);
        final AspectRatioBox widthFitsSoHeightIsNotChanged = aspectRatioBox.fitHeightThenWidth(1600, 1024);
        assertEquals(aspectRatioBox, widthFitsSoHeightIsNotChanged);
        final AspectRatioBox heightFitsSoWidthIsNotChanged = aspectRatioBox.fitHeightThenWidth(1900, 900);
        assertEquals(aspectRatioBox, heightFitsSoWidthIsNotChanged);
    }

    @Test
    public void fitHeightThenWidthPreservesAspect() throws Exception {
        final int initialWidth = 400;
        final int initialHeight = 500;

        final AspectRatioBox aspectRatioBox = new AspectRatioBox(initialWidth, initialHeight);

        final int extra = 200;
        {
            final int maxWidth = 200;
            final int maxHeight = 250;
            final AspectRatioBox result = aspectRatioBox.fitHeightThenWidth(maxWidth, maxHeight + extra);
            assertEquals(new AspectRatioBox(maxWidth, maxHeight), result);
        }
        {
            final int maxWidth = 200;
            final int maxHeight = 250;
            final AspectRatioBox result = aspectRatioBox.fitHeightThenWidth(maxWidth + extra, maxHeight);
            assertEquals(new AspectRatioBox(maxWidth, maxHeight), result);
        }
        {
            final int maxWidth = 800;
            final int maxHeight = 1000;
            final AspectRatioBox result = aspectRatioBox.fitHeightThenWidth(maxWidth, maxHeight + extra);
            assertEquals(aspectRatioBox, result);
        }
        {
            final int maxWidth = 800;
            final int maxHeight = 1000;
            final AspectRatioBox result = aspectRatioBox.fitHeightThenWidth(maxWidth + extra, maxHeight);
            assertEquals(aspectRatioBox, result);
        }
    }
}
