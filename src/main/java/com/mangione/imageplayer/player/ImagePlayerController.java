package com.mangione.imageplayer.player;

import com.mangione.imageplayer.container.MediaContainerPanel;
import com.mangione.imageplayer.file.FileLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;


public class ImagePlayerController implements ImagePlayerControllerInterface, PlaybackListener {
	private final FileLoader fileLoader;
	private final MediaContainerPanel mediaContainerPanel;
	private final PlaybackEngine playbackEngine;
	private ImagePlayerPanel playerFrame;

	public ImagePlayerController(File rootDirectory) throws Exception {
		fileLoader = new FileLoader(rootDirectory);
		mediaContainerPanel = new MediaContainerPanel();
		playbackEngine = new PlaybackEngine(this);

		createAndDisplayPlayerFrame();
		fileLoader.getNextFile();
		displayCurrentFile();
		playbackEngine.start();
	}

	@Override
	public void onTimerExpired() {
		fileLoader.getNextFile();
		displayCurrentFile();
	}

	@Override
	public void increasePlaybackSpeed() {
		playbackEngine.increaseSpeed();
	}

	@Override
	public void decreasePlaybackSpeed() {
		playbackEngine.decreaseSpeed();
	}

	private void displayCurrentFile() {
		try {
			PlayerPanel playerPanel = ImagePanelFactory.create(fileLoader.getCurrentFile().getFile().getPath());
			playbackEngine.setDuration(playerPanel.getDurationMillis());
			String status = String.format("%d of %d: %s",
					fileLoader.getCurrentIndex(), fileLoader.getNumFilesLoaded(),
					fileLoader.getCurrentFile().getFile().getName());
			mediaContainerPanel.display(playerPanel, status);
		} catch (Exception e) {
			e.printStackTrace();
			playbackEngine.setDuration(0);
		}
	}

	private synchronized void createAndDisplayPlayerFrame() {
		Dimension frameSize;
		Point location;
		boolean nextUndecorated;
		float transparency;

		if (playerFrame == null) {
			frameSize = new Dimension(300, 400);
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			location = new Point(Math.max(0, screenSize.width - frameSize.width), 0);
			nextUndecorated = false;
			transparency = 1.0f;
		} else {
			frameSize = playerFrame.getSize();
			location = playerFrame.getLocation();
			nextUndecorated = !playerFrame.isUndecorated();
			transparency = playerFrame.getTransparency();
			playerFrame.dispose();
		}

		playerFrame = new ImagePlayerPanel(mediaContainerPanel, nextUndecorated, frameSize, location, transparency);
		playerFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SwingUtilities.invokeLater(ImagePlayerController.this::createAndDisplayPlayerFrame);
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
						fileLoader.getPreviousFile();
						displayCurrentFile();
						break;
					case KeyEvent.VK_PERIOD:
						fileLoader.getNextFile();
						displayCurrentFile();
						break;
					case KeyEvent.VK_T:
						playerFrame.adjustTransparency(-0.02f);
						break;
					case KeyEvent.VK_Y:
						playerFrame.adjustTransparency(0.02f);
						break;
					case KeyEvent.VK_D:
						if (fileLoader.getCurrentFile().getFile().delete()) {
							fileLoader.getNextFile();
							displayCurrentFile();
						}
						break;
					case KeyEvent.VK_R:
						fileLoader.random();
						fileLoader.getNextFile();
						displayCurrentFile();
						break;
				}
			}
		});
	}
}
