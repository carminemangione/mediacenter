package com.mangione.mediacenter.view.moviebrowser;

import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.panels.GradientPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * User: carminemangione
 * Date: Aug 8, 2009
 * Time: 4:18:44 PM
 */
public class MovieBrowser extends GradientPanel {
    private final static int NUMBER_OF_COLUMNS = 5;
    private final static double ASPECT_RATIO_OF_ICON = 0.70;
    private final static double PERCENT_SCEEN_HEIGHT_OF_FILE_NAME = 0.20;
    private final Dimension imageSize;
    private final Dimension screenSize;
    private final int leftRightBorderSize;
    private final int heightOfTitle;
    private int currentColumn = 0;
    private int currentRow = 0;
    private int numberOfLines;
    private VideoFiles videoFiles;
    private int fractionOfImageBetweenImages = 5;
    private int currentTopOfImage;
    private int currentSelectedRow = currentRow;
    private Font movieTitleFont;
    private boolean dimm = false;
    private boolean animating = false;

    public MovieBrowser(Dimension screenSize, VideoFiles movieDirs) throws Exception {
        this.screenSize = screenSize;
        setOpaque(false);
        heightOfTitle = (int) (screenSize.height * PERCENT_SCEEN_HEIGHT_OF_FILE_NAME);
        double heightOfImage = (screenSize.height - heightOfTitle) * fractionOfImageBetweenImages / (1 + (2 * fractionOfImageBetweenImages));
        imageSize = new Dimension((int) (heightOfImage * ASPECT_RATIO_OF_ICON), (int) heightOfImage);
        leftRightBorderSize = (screenSize.width - NUMBER_OF_COLUMNS * imageSize.width) / 5;
        numberOfLines = movieDirs.getNumberOfVideoFiles() / NUMBER_OF_COLUMNS + 1;
        videoFiles = movieDirs;
        movieTitleFont = new Font(Font.SANS_SERIF, Font.BOLD, 50);
        setPreferredSize(screenSize);
    }

    public void setDim(boolean dim) {
        this.dimm = dim;
        invalidate();
        repaint();
    }

    public void zoomToLetter(char charPressed) {
        int newIndex = videoFiles.getIndexFirstVideoStart(charPressed);
        currentRow = newIndex / NUMBER_OF_COLUMNS;
        currentSelectedRow = currentRow;
        currentColumn = newIndex % NUMBER_OF_COLUMNS;
        repaint();
    }

    public synchronized void arrowPressed(KeyEvent arrowPressedEvent, boolean repeat) throws Exception {
        int newRow = adjustRowAndCurrentColumnForArrow(arrowPressedEvent);

        newRow = moveUpIfScrolledOffLeft(newRow);
        newRow = moveDownIfScrolledOffRight(newRow);
        newRow = stopScrollAtTopOrBottom(newRow);

        if (newRow != currentRow) {
            if (!animating) {
                if (repeat) {
                    currentRow = newRow;
                    currentSelectedRow = newRow;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            invalidate();
                            repaint();
                        }
                    });
                } else {
                    animateRowChange(newRow);
                }
            }
        } else {
            repaint();
        }
    }

    public VideoFile getCurrentVideoFile() {
        return videoFiles.getVideoFile(getIndexOfSelected());
    }

    public void setVideoFiles(VideoFiles videoFiles) {
        this.videoFiles = videoFiles;
        numberOfLines = videoFiles.getNumberOfVideoFiles() / NUMBER_OF_COLUMNS + 1;
        repaint();
    }

    @Override
    public synchronized void paint(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;

        Color oldColor = graphics2d.getColor();
        Stroke oldStroke = graphics2d.getStroke();
        if (videoFiles.getNumberOfVideoFiles() > 0) {
            final int indexOfSelected = getIndexOfSelected();

            graphics2d.setStroke(new BasicStroke(4f));
            graphics2d.setColor(Color.white);
            graphics2d.drawLine(0, heightOfTitle - 4, screenSize.width, heightOfTitle - 4);

            String movieName = videoFiles.getVideoFile(indexOfSelected).getVideoName();
            graphics2d.setFont(movieTitleFont);
            graphics2d.setColor(Color.LIGHT_GRAY);
            graphics2d.drawString(movieName, 30, heightOfTitle / 2);

            graphics2d.setClip(0, heightOfTitle, screenSize.width, screenSize.height);
            int topOfCurrentImageRow = currentTopOfImage;

            paintThreeRowsOfImagesLeavingBlankRowAtTop(graphics2d, indexOfSelected, topOfCurrentImageRow);
            Composite oldAlphaComposite = graphics2d.getComposite();

            if (dimm) {
                graphics2d.setColor(Color.black);
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                graphics2d.setComposite(ac);
                graphics2d.fillRect(0, 0, screenSize.width, screenSize.height);
            }

            int currentBottomOfGradient = heightOfTitle;
            int heightToMask = imageSize.height / 6;
            addGradient(graphics2d, currentBottomOfGradient, heightToMask, true, Color.black);

            currentBottomOfGradient = screenSize.height;
            addGradient(graphics2d, currentBottomOfGradient, heightToMask, false, Color.black);

            graphics2d.setComposite(oldAlphaComposite);

        } else {
            graphics2d.setFont(movieTitleFont);
            graphics2d.setColor(Color.LIGHT_GRAY);
            graphics2d.drawString("No movies found... Add a directory", 50, screenSize.height / 2 - 20);

        }
        graphics2d.setColor(oldColor);
        graphics2d.setStroke(oldStroke);
        super.paint(graphics);
    }

    private void paintThreeRowsOfImagesLeavingBlankRowAtTop(Graphics2D graphics2d, int indexOfSelected, int topOfCurrentImageRow) {
        int startingRow;
        if (currentRow > 0) {
            startingRow = currentRow - 1;
        } else {
            startingRow = 0;
            topOfCurrentImageRow += calculateHeightOfOneRow();
        }
        for (int i = startingRow; i < startingRow + 3; i++) {
            paintOneRowOfImages(graphics2d, indexOfSelected, i, topOfCurrentImageRow);
            topOfCurrentImageRow += calculateHeightOfOneRow();
        }
    }

    private int calculateHeightOfOneRow() {
        return imageSize.height * (1 + fractionOfImageBetweenImages) / fractionOfImageBetweenImages;
    }

    private int getIndexOfSelected() {
        return Math.min(currentSelectedRow * NUMBER_OF_COLUMNS + currentColumn, videoFiles.getNumberOfVideoFiles() - 1);
    }

    private void animateRowChange(final int newRow) {
        animating = true;
        final boolean animateDown = newRow < currentRow;
        currentSelectedRow = newRow;
        int centerOfSelectedRow = (screenSize.height - heightOfTitle) / 2 + heightOfTitle;
        final int topOfFirstRowBeforeScroll = centerOfSelectedRow - imageSize.height *
                (3 * fractionOfImageBetweenImages + 2) / 2 / fractionOfImageBetweenImages;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentTopOfImage = topOfFirstRowBeforeScroll;
                int moveEachStep = 1;
                int numberOfSteps = calculateHeightOfOneRow();
                for (int i = 1; i < numberOfSteps; i++) {
                    currentTopOfImage += (animateDown ? moveEachStep : -moveEachStep);
                    repaint();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // nowarn
                    }
                }
                currentTopOfImage = topOfFirstRowBeforeScroll;
                currentRow = newRow;
                repaint();
                animating = false;
            }
        });
    }


    private void paintOneRowOfImages(Graphics2D graphics2d, int indexOfSelected, int row, int topOfImage) {
        int currentIndex = row * NUMBER_OF_COLUMNS;
        for (int column = 0; column < NUMBER_OF_COLUMNS && currentIndex < videoFiles.getNumberOfVideoFiles(); column++) {
            int leftOfImage = getLeftOfImage(column);

            ImageIcon currentImage = videoFiles.getVideoFile(currentIndex).getImageIcon();
            graphics2d.drawImage(currentImage.getImage(), leftOfImage, topOfImage, imageSize.width,
                    imageSize.height, null);
            if (currentIndex == indexOfSelected) {
                graphics2d.setColor(Color.green);
                graphics2d.setStroke(new BasicStroke(6));
                graphics2d.drawRoundRect(leftOfImage - 6, topOfImage - 6, imageSize.width + 6, imageSize.height + 6, 4, 4);
            }
            currentIndex++;
        }
    }

    private int getLeftOfImage(int column) {
        return column * (imageSize.width + leftRightBorderSize) + leftRightBorderSize / 2;
    }

    private int stopScrollAtTopOrBottom(int newRow) {
        if (newRow < 0) {
            newRow = 0;
        }

        if (newRow == numberOfLines) {
            newRow = numberOfLines - 1;
        }
        return newRow;
    }

    private int moveDownIfScrolledOffRight(int newRow) {
        if (currentColumn == NUMBER_OF_COLUMNS) {
            if (newRow < numberOfLines - 1) {
                newRow++;
                currentColumn = 0;
            } else {
                newRow = numberOfLines - 1;
                currentColumn = NUMBER_OF_COLUMNS - 1;
            }
        }
        return newRow;
    }

    private int moveUpIfScrolledOffLeft(int newRow) {
        if (currentColumn < 0) {
            if (newRow > 0) {
                currentColumn = NUMBER_OF_COLUMNS - 1;
                newRow--;
            } else {
                newRow = 0;
                currentColumn = 0;
            }
        }
        return newRow;
    }

    private int adjustRowAndCurrentColumnForArrow(KeyEvent arrowPressedEvent) {
        int newRow = currentRow;
        switch (arrowPressedEvent.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                currentColumn--;
                break;

            case KeyEvent.VK_RIGHT:
                currentColumn++;
                break;

            case KeyEvent.VK_UP:
                newRow--;
                break;

            case KeyEvent.VK_DOWN:
                newRow++;
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
        return newRow;
    }

}
