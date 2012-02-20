package com.mangione.imageplayer;

import com.mangione.imageplayer.buttonpanel.ButtonPanelControllerInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: carminemangione
 * Date: Feb 23, 2009
 * Time: 2:29:33 AM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class ImagePlayerController implements ButtonPanelControllerInterface, ImagePlayerControllerInterface {
    private final Random RANDOM = new Random();
    private int currentIndex;
    private boolean pausePressed;
    private List<FileWithComparString> imageFiles = Collections.synchronizedList(new ArrayList<FileWithComparString>());
    private boolean running;
    private Thread thread;
    private ImagePanelController imagePanelController;
    private long lastPicTimeMillis;
    private JLabel imageFileNameLabel = new JLabel("<no image>", JLabel.CENTER);
    private int timeBetweenPhotos = 3000;
    private boolean undecorated = false;
    private ImagePlayerPanel playerFrame;
    private JPanel imagePanelWithTitle;
    private float currentTransparency = 1.0f;
    private final static Pattern NUMBER_MATCH = Pattern.compile("\\d+");
    private final static String[] EXTENSIONS = {".jpg", ".jpeg", ".png"};



    public ImagePlayerController() {

        imageFileNameLabel.setForeground(Color.DARK_GRAY);
        imageFileNameLabel.setFont(imageFileNameLabel.getFont().deriveFont(Font.ITALIC, 9.0f));
        imageFileNameLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
        File currentFile = new File("/Users/carminemangione/Pictures/internet/allpics/");
        recurseAndCollectFiles(currentFile);
        System.out.println("currentFile = " + imageFiles.size());

        Collections.sort(imageFiles);

        imagePanelController = new ImagePanelController(imageFiles.get(RANDOM.nextInt(imageFiles.size())).file.getPath());

        imagePanelWithTitle = new JPanel(new BorderLayout());
        imagePanelWithTitle.add(imagePanelController.getImagePanel(), BorderLayout.CENTER);
        imagePanelWithTitle.add(imageFileNameLabel, BorderLayout.SOUTH);

        createAndDisplayPlayerFrame();
        startPlayer(imagePanelController);


    }

    private void recurseAndCollectFiles(File currentFile) {
        for (File file : currentFile.listFiles()) {
            if (file.isDirectory()) {
                recurseAndCollectFiles(file);
            } else {
                String name = file.getName();
                boolean imageFile = false;
                for (int i = 0; i < EXTENSIONS.length && !imageFile; i++) {
                    imageFile = name.endsWith(EXTENSIONS[i]);

                }
                if (imageFile && !file.getPath().contains("WICThumbs") && !name.contains("DFTTH")) {
                    imageFiles.add(new FileWithComparString(file));
                }

            }
        }
    }

    private synchronized void createAndDisplayPlayerFrame() {
        Dimension frameSize;
        Point location;
        if (playerFrame == null) {
            frameSize = new Dimension(300, 400);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int windowX = Math.max(0, (screenSize.width - frameSize.width));
            location = new Point(windowX, 0);
        } else {
            frameSize = playerFrame.getSize();
            location = playerFrame.getLocation();
            playerFrame.dispose();
        }

        playerFrame = new ImagePlayerPanel(imagePanelWithTitle, undecorated, frameSize, location);
        playerFrame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        undecorated = !undecorated;
                        createAndDisplayPlayerFrame();
                    }
                });
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
        playerFrame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {

                switch (event.getKeyCode()) {
                    case KeyEvent.VK_EQUALS:
                        increasePlaybackSpeed();
                        break;
                    case KeyEvent.VK_MINUS:
                        decreasePlaybackSpeed();
                        break;
                    case KeyEvent.VK_COMMA:
                        currentIndex--;
                        break;
                    case KeyEvent.VK_PERIOD:
                        currentIndex++;
                        break;
                    case KeyEvent.VK_T:
                        currentTransparency = Math.max(0, currentTransparency - 0.02f);
                        playerFrame.getRootPane().putClientProperty("Window.alpha", currentTransparency);
                        break;
                    case KeyEvent.VK_Y:
                        currentTransparency = Math.min(1, currentTransparency + 0.02f);
                        playerFrame.getRootPane().putClientProperty("Window.alpha", currentTransparency);
                        break;
                    case KeyEvent.VK_D:
                        if (imageFiles.get(currentIndex).file.delete()) {
                            imageFileNameLabel.setText("Deleted: " + imageFiles.get(currentIndex).file.getName());
                        } else {
                            imageFileNameLabel.setText("Deleted failed for: " + imageFiles.get(currentIndex).file.getName());
                        }
                        break;
                    case KeyEvent.VK_R:
                        currentIndex = RANDOM.nextInt(imageFiles.size());
                        break;
                }

            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        new ImagePlayerController();
    }

    @Override
    public void stopPressed() {
        if (thread.isAlive()) {
            running = false;
        }

    }

    @Override
    public void deletePressed() {
        pausePressed = true;
        File fileToDelete = imageFiles.remove(currentIndex).file;
        //noinspection ResultOfMethodCallIgnored
        fileToDelete.delete();
        currentIndex--;
        resume();
    }

    @Override
    public void backPressed() {
        currentIndex--;
    }

    @Override
    public void fastForwardPressed() {

    }

    @Override
    public void forwardPressed() {
        currentIndex++;

    }

    @Override
    public void pausePressed() {
        pausePressed = true;

    }

    @Override
    public void playPressed() {
        if (thread.isAlive()) {
            resume();
        } else {
            startPlayer(imagePanelController);
        }
    }

    @Override
    public void increasePlaybackSpeed() {
        timeBetweenPhotos += 500;
    }

    @Override
    public void decreasePlaybackSpeed() {
        timeBetweenPhotos = Math.max(timeBetweenPhotos - 500, 500);
    }

    private void resume() {
        pausePressed = false;
        synchronized (this) {
            notifyAll();
        }
    }

    private void startPlayer(final ImagePanelController imagePanelController) {
        running = true;
        thread = new Thread() {
            @Override
            public void run() {
                currentIndex = RANDOM.nextInt(imageFiles.size());
                int currentDisplayedIndex = -1;
                while (running) {
                    File currentImageFile;
                    try {
                        synchronized (ImagePlayerController.this) {
                            if (pausePressed) {
                                ImagePlayerController.this.wait();
                            }
                        }
                        if (currentDisplayedIndex != currentIndex) {
                            synchronized (this) {
                                currentImageFile = imageFiles.get(currentIndex).file;
                                imageFileNameLabel.setText(currentIndex + " of " + imageFiles.size() + ": " + currentImageFile.getName());
                                imagePanelController.setImage(currentImageFile.getPath());
                                lastPicTimeMillis = System.currentTimeMillis();
                                currentDisplayedIndex = currentIndex;
                            }
                        }

                        long currentTime = System.currentTimeMillis();
                        long timeSinceLastSwitch = currentTime - lastPicTimeMillis;
                        if (timeSinceLastSwitch > timeBetweenPhotos) {
                            currentIndex++;
                            currentIndex = currentIndex % imageFiles.size();
                        } else {
                            Thread.sleep(10);

                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        lastPicTimeMillis = System.currentTimeMillis();
        thread.start();
    }

    private class FileWithComparString implements Comparable<FileWithComparString> {
        private File file;
        private String compareString;
        private Long number;

        private FileWithComparString(File file) {
            this.file = file;
            this.compareString = getLeadingStringStrippedOfCopyNumber(file.getName());
            Matcher m = NUMBER_MATCH.matcher(compareString);
            if (m.matches()) {
                int startDigit = compareString.length() > 16 ? compareString.length() - 16 : 0;
                number = Long.parseLong(compareString.substring(startDigit));
            }
        }

        private String getLeadingStringStrippedOfCopyNumber(String name) {

            String nameWithoutExtension = name.replace(".jpg", "").replace(".jpeg", "");
            boolean oneDigitVersion = name.length() > 2 && name.charAt(name.length() - 2) == '-';
            boolean twoDigitVersion = name.length() > 3 && name.charAt(name.length() - 3) == '-';
            String strippedOfCopyNumber = nameWithoutExtension;
            if (oneDigitVersion || twoDigitVersion) {
                strippedOfCopyNumber = name.substring(0, name.lastIndexOf("-") - 1);

            }
            return strippedOfCopyNumber.replace("-", "");
        }

        @Override
        public int compareTo(FileWithComparString other) {
            int compareTo;
            if (compareString.equals(other.compareString)) {
                compareTo = file.lastModified() > other.file.lastModified() ? 1 : file.lastModified() == other.file.lastModified() ? 0 : -1;
            } else if (number != null && other.number != null) {
                compareTo = number.compareTo(other.number);
            } else {
                compareTo = compareString.compareTo(other.compareString);
            }
            return compareTo; 
        }
    }
}
