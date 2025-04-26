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


public class ImagePlayerController implements ButtonPanelControllerInterface, ImagePlayerControllerInterface {
	private final FileLoader fileLoader;
	private final JPanel imagePanelWithTitle;
	private final JLabel imageFileNameLabel = new JLabel("<no image>", JLabel.CENTER);
	private final JPanel containerPanel;
	private boolean pausePressed;
	private boolean running;
	private Thread thread;
	private long lastPicTimeMillis;
	private int timeBetweenPhotos = 3000;
	private boolean undecorated = false;
	private ImagePlayerPanel playerFrame;
	private float currentTransparency = 1.0f;
	private FileWithCompareString currentFile;
	private FileWithCompareString lastFile;

	private ImagePlayerController() throws Exception {

		imageFileNameLabel.setForeground(Color.DARK_GRAY);
		imageFileNameLabel.setFont(imageFileNameLabel.getFont().deriveFont(Font.ITALIC, 9.0f));
		imageFileNameLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
//		File rootDirectory = new File("temp");
		File rootDirectory = new File("/Volumes/Pictures");
		fileLoader = new FileLoader(rootDirectory);
		currentFile = fileLoader.getNextFile();
		ImagePanelFactory imagePanelFactory = new ImagePanelFactory(fileLoader.getNextFile().getFile().getAbsolutePath());
		containerPanel = new JPanel(new BorderLayout());
		containerPanel.add(imagePanelFactory.getImagePanel(), BorderLayout.CENTER);
		imagePanelWithTitle = new JPanel(new BorderLayout());
		imagePanelWithTitle.add(containerPanel, BorderLayout.CENTER);
		imagePanelWithTitle.add(imageFileNameLabel, BorderLayout.SOUTH);

		createAndDisplayPlayerFrame();
		startPlayer();
	}

	public static void main(String[] args) throws Exception {
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
		//noinspection ResultOfMethodCallIgnored
		currentFile.getFile().delete();
		resume();
	}

	@Override
	public void backPressed() {
		currentFile = fileLoader.getPreviousFile();
	}

	@Override
	public void fastForwardPressed() {
		currentFile = fileLoader.getNextFile();
	}

	@Override
	public void forwardPressed() {
		currentFile = fileLoader.getNextFile();

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
			startPlayer();
		}
	}

	@Override
	public void increasePlaybackSpeed() {
		timeBetweenPhotos += 500;
	}

	@Override
	public void decreasePlaybackSpeed() {
		timeBetweenPhotos = Math.max(timeBetweenPhotos - 5000, 5000);
	}

	private void resume() {
		pausePressed = false;
		synchronized (this) {
			notifyAll();
		}
	}

	private void startPlayer() {
		running = true;
		thread = new Thread() {
			@Override
			public void run() {

				while (running) {
						synchronized (ImagePlayerController.this) {
							if (pausePressed) {
								try {
									ImagePlayerController.this.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}

						synchronized (this) {
							if (lastFile == null || !lastFile.equals(currentFile)) {
								try {
									final JComponent imagePanel = new ImagePanelFactory(currentFile.getFile().getPath()).getImagePanel();
									SwingUtilities.invokeLater(() -> {
										imageFileNameLabel.setText(String.format("%d of %d: %s", fileLoader.getCurrentIndex(),
												fileLoader.getNumFilesLoaded(), currentFile.getFile().getName()));
										containerPanel.removeAll();
										containerPanel.add(imagePanel, BorderLayout.CENTER);
									});
									timeBetweenPhotos = currentFile.getFile().getName().endsWith(".mp4") ? 10000 : 5000;
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("Could not load file: " + currentFile.getFile().getAbsolutePath());
									timeBetweenPhotos = 0;
								}
								lastFile = currentFile;

							}
						}

						long currentTime = System.currentTimeMillis();
						long timeSinceLastSwitch = currentTime - lastPicTimeMillis;
						if (timeSinceLastSwitch > timeBetweenPhotos) {
							loadNextFile();
						} else {

							try {
								//noinspection BusyWait
								Thread.sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
				}
			}

		};
		lastPicTimeMillis = System.currentTimeMillis();
		thread.start();
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
				SwingUtilities.invokeLater(() -> {
					undecorated = !undecorated;
					createAndDisplayPlayerFrame();
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
						currentFile = fileLoader.getPreviousFile();
						break;
					case KeyEvent.VK_PERIOD:
						currentFile = fileLoader.getNextFile();
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
						if (currentFile.getFile().delete()) {
							imageFileNameLabel.setText("Deleted: " + currentFile.getFile().getName());
							loadNextFile();
						} else {
							imageFileNameLabel.setText("Deleted failed for: " + currentFile.getFile().getName());
						}
						break;
					case KeyEvent.VK_R:
						fileLoader.random();
						break;
				}

			}
		});
	}

	private void loadNextFile() {
		currentFile = fileLoader.getNextFile();
		lastPicTimeMillis = System.currentTimeMillis();
	}

}
