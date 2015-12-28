package com.mangione.imagedupfinder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * User: carminemangione
 * Date: Jul 26, 2009
 * Time: 2:53:16 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class ImageStats {
    private final static int BLOCK_SIZE = 64;
    private final int[] blockAverageValues;
    private int height;
    private int width;
    private File imageFile;

    public ImageStats(File imageFile) throws Exception {
        this.imageFile = imageFile;

        List<Integer> blockAverageValues = new ArrayList<>();

        BufferedImage bufferedImage = ImageIO.read(imageFile);
        if (bufferedImage != null) {
            int currentRowForBlock = 0;
            int currentColForBlock = 0;

            height = bufferedImage.getHeight();
            width = bufferedImage.getWidth();
            while (currentRowForBlock < height || currentColForBlock < width) {
                int average = 0;
                for (int row = 0; row < BLOCK_SIZE && currentRowForBlock + row < height; row++) {
                    for (int col = 0; col < BLOCK_SIZE && currentColForBlock + col < width; col++) {
                        average += bufferedImage.getRGB(currentColForBlock + col, currentRowForBlock + row);
                    }
                }
                blockAverageValues.add(average / (BLOCK_SIZE * BLOCK_SIZE));
                currentColForBlock += BLOCK_SIZE;
                currentRowForBlock += BLOCK_SIZE;
            }
        }


        this.blockAverageValues = new int[blockAverageValues.size()];
        for (int i = 0; i < this.blockAverageValues.length; i++) {
            this.blockAverageValues[i] = blockAverageValues.get(i);

        }
    }

    public boolean match(int percent, ImageStats other) {
        boolean match = false;
        int percentHeight = getPercentDif(height - other.height, other.height);
        int percentWidth = getPercentDif(width - other.width, other.width);
        if (percentHeight >= percent && percentWidth > percent) {
            int levenshtienDistance = getLevenshteinDistance(blockAverageValues, other.blockAverageValues);
            match = getPercentDif(Math.abs(levenshtienDistance), blockAverageValues.length) >= percent;
        }
        return match;
    }

    public File getImageFile(){
        return imageFile;    
    }

    private int getPercentDif(int value, int otherValue) {
        return (int)(100f - (100f * ((float)value/(float)otherValue)));
    }

    public static int getLevenshteinDistance(int[] s, int[] t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        /*
          The difference between this impl. and the previous is that, rather
           than creating and retaining a matrix of size s.length()+1 by t.length()+1,
           we maintain two single-dimensional arrays of length s.length()+1.  The first, d,
           is the 'current working' distance array that maintains the newest distance cost
           counts as we iterate through the characters of String s.  Each time we increment
           the index of String t we are comparing, d is copied to p, the second int[].  Doing so
           allows us to retain the previous cost counts as required by the algorithm (taking
           the minimum of the cost count to the left, up one, and diagonally up and to the left
           of the current cost count being calculated).  (Note that the arrays aren't really
           copied anymore, just switched...this is clearly much better than cloning an array
           or doing a System.arraycopy() each time  through the outer loop.)

           Effectively, the difference between the two implementations is this one does not
           cause an out of memory condition when calculating the LD over two very large strings.
        */

        int n = s.length; // length of s
        int m = t.length; // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        int p[] = new int[n + 1]; //'previous' cost array, horizontally
        int d[] = new int[n + 1]; // cost array, horizontally
        int _d[]; //placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        int t_j; // jth character of t

        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            t_j = t[j - 1];
            d[0] = j;

            for (i = 1; i <= n; i++) {
                cost = s[i - 1] == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }


}
