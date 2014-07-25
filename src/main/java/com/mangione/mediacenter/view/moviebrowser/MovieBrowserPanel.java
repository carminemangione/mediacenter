package com.mangione.mediacenter.view.moviebrowser;

import com.mangione.mediacenter.model.videofile.VideoFile;
import com.mangione.mediacenter.model.videofile.VideoFiles;
import com.mangione.mediacenter.view.SharedConstants;
import com.mangione.mediacenter.view.components.GradientPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


public class MovieBrowserPanel extends GradientPanel {
    private final static int NUMBER_OF_COLUMNS = 5;
    private final static double ASPECT_RATIO_OF_POSTER = 0.70;
    private final static double HORIZONTAL_BORDER = 0.02;
    private final static double VERTICAL_BORDER = 0.02;

    private int currentColumn = 0;
    private int currentRow = 0;
    private int numberOfLines;
    private VideoFiles videoFiles;
    private int currentSelectedRow = currentRow;

    private boolean dim = false;
    private boolean animating = false;
    private int columnWidth;
    private int posterWidth;
    private int posterHeight;
    private double rowHeight;
    private Font movieTitleFont;
    private int horizontalBorder;
    private int verticalBorder;
    private int currentTopOfImage;

    public MovieBrowserPanel(VideoFiles movieDirs) throws Exception {
        numberOfLines = movieDirs.getNumberOfVideoFiles() / NUMBER_OF_COLUMNS + 1;
        videoFiles = movieDirs;
        movieTitleFont = new Font(Font.SANS_SERIF, Font.BOLD, 50);
        setOpaque(false);
    }

    public void setDim(boolean dim) {
        this.dim = dim;
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
                    SwingUtilities.invokeLater(() -> {
                        invalidate();
                        repaint();
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
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    public synchronized void paintComponent(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;
        Dimension screenSize = getSize();
        horizontalBorder = (int) (screenSize.getWidth() * HORIZONTAL_BORDER);
        verticalBorder = (int) (screenSize.getHeight() * VERTICAL_BORDER);

        columnWidth = (int) (screenSize.getWidth() / NUMBER_OF_COLUMNS);

        posterWidth = columnWidth - horizontalBorder * 2;
        posterHeight = (int) ((double) posterWidth / ASPECT_RATIO_OF_POSTER);
        rowHeight = posterHeight + 2 * verticalBorder;

        int numberOfRowsThatFitOnTheScreen = (int) Math.ceil(screenSize.getHeight() / (posterHeight + 2 * verticalBorder));

        Color oldColor = graphics2d.getColor();
        if (videoFiles.getNumberOfVideoFiles() > 0) {
            final int indexOfSelected = getIndexOfSelected();

            paintRowsOfPosters(graphics2d, indexOfSelected, numberOfRowsThatFitOnTheScreen);
            Composite oldAlphaComposite = graphics2d.getComposite();

            if (dim) {
                graphics2d.setColor(Color.black);
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
                graphics2d.setComposite(ac);
                graphics2d.fillRect(0, 0, screenSize.width, screenSize.height);
            }

            int heightToMask = posterHeight / 2;
            addGradient(graphics2d, 0, heightToMask, screenSize.width, true, SharedConstants.DEFAULT_BACKGROUND_COLOR);
            addGradient(graphics2d, screenSize.height, heightToMask, screenSize.width, false, SharedConstants.DEFAULT_BACKGROUND_COLOR);

            graphics2d.setComposite(oldAlphaComposite);

        } else {
            final Font oldStroke = graphics2d.getFont();
            graphics2d.setFont(movieTitleFont);
            graphics2d.setColor(Color.LIGHT_GRAY);
            graphics2d.drawString("No movies found... Add a directory", 50, screenSize.height / 2 - 20);
            graphics2d.setFont(oldStroke);

        }
        graphics2d.setColor(oldColor);
    }


    private void paintRowsOfPosters(Graphics2D graphics2d, int indexOfSelected, int numberOfRows) {
        int startingRow;
        int topOfCurrentImageRow = verticalBorder + currentTopOfImage;
        if (currentRow > 0) {
            startingRow = currentRow - 1;
        } else {
            startingRow = 0;
            topOfCurrentImageRow += rowHeight;
        }
        for (int i = startingRow; i < numberOfRows + 3; i++) {
            paintOneRowOfPosters(graphics2d, indexOfSelected, i, topOfCurrentImageRow);
            topOfCurrentImageRow += rowHeight;
        }
    }


    private int getIndexOfSelected() {
        return Math.min(currentSelectedRow * NUMBER_OF_COLUMNS + currentColumn, videoFiles.getNumberOfVideoFiles() - 1);
    }

    private void animateRowChange(final int newRow) {
        new Thread() {
            public void run() {
                animating = true;
                final boolean animateDown = newRow < currentRow;
                currentSelectedRow = newRow;

                currentTopOfImage = 0;
                int moveEachStep = 1;
                int numberOfSteps = (int) rowHeight / moveEachStep;
                for (int i = 1; i < numberOfSteps; i++) {
                    currentTopOfImage += (animateDown ? moveEachStep : -moveEachStep);
                    repaint();
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        // nowarn
                    }
                }
                currentTopOfImage = 0;
                currentRow = newRow;
                repaint();
                animating = false;
            }
        }.start();

    }


    private void paintOneRowOfPosters(Graphics2D graphics2d, int indexOfSelected, int row, int topOfImage) {
        int currentIndex = row * NUMBER_OF_COLUMNS;
        for (int column = 0; column < NUMBER_OF_COLUMNS && currentIndex < videoFiles.getNumberOfVideoFiles(); column++) {
            int leftOfImage = columnWidth * column + horizontalBorder;

            ImageIcon currentImage = videoFiles.getVideoFile(currentIndex).getImageIcon();
            graphics2d.drawImage(currentImage.getImage(), leftOfImage, topOfImage, posterWidth,
                    posterHeight, null);
            if (currentIndex == indexOfSelected) {
                graphics2d.setColor(Color.green);
                int strokeHighlightWidth = 2;
                graphics2d.setStroke(new BasicStroke(strokeHighlightWidth));
                graphics2d.drawRoundRect(leftOfImage - strokeHighlightWidth, topOfImage - strokeHighlightWidth,
                        posterWidth + strokeHighlightWidth, posterHeight + strokeHighlightWidth, 4, 4);
            }
            currentIndex++;
        }
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
