package com.mangione.imageplayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class SwingImagePanel implements PlayerPanel {
	private Dimension imageSize;
	private final String currentImageFile;
	private final JLabel jLabel;

	private final JComponent panel;


	SwingImagePanel(String currentImageFile) {
		panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(Color.DARK_GRAY);
		this.currentImageFile = currentImageFile;
		panel.setLayout(new BorderLayout());

		jLabel = new JLabel(new ImageIcon(currentImageFile));
		jLabel.setOpaque(false);
		panel.add(jLabel, BorderLayout.CENTER);
		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				SwingUtilities.invokeLater(() -> {
					ImageIcon currentImage = getCurrentImage();
					if (currentImage != null) {
						jLabel.setIcon(currentImage);
						panel.repaint();
					}
				});
			}
		});

	}

	private ImageIcon getCurrentImage() {
		final ImageIcon imageIcon = loadImage();
		if (imageIcon != null) {
			final QuadDimension quadDimension = new QuadDimension(imageIcon.getImage());
			return new ImageIcon(imageIcon.getImage().getScaledInstance(quadDimension.size.width,
					quadDimension.size.height, Image.SCALE_REPLICATE));
		}
		return null;
	}

	private ImageIcon loadImage() {
		final ImageLoaderThread imageLoaderThread = new ImageLoaderThread(currentImageFile);
		imageLoaderThread.start();
		try {
			imageLoaderThread.join(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (imageLoaderThread.isAlive()) {
			imageLoaderThread.interrupt();
			System.out.println("Failed to load " + currentImageFile);
		}
		return imageLoaderThread.getImageIcon();
	}

	public Dimension getMaximumSize() {
		return imageSize;
	}

	@Override
	public JComponent getPanel() {
		return panel;
	}

	@Override
	public int getDurationMillis() {
		return 5000;
	}

	private class QuadDimension {
		private final Dimension size;

		QuadDimension(Image currentImage) {
			Dimension panelSize = panel.getSize();

			double panelWidth = panelSize.getWidth();
			double panelHeight = panelSize.getHeight();

			imageSize = new Dimension(currentImage.getWidth(null),
					currentImage.getHeight(null));
			double imageWidth = currentImage.getWidth(null) * 2;
			double imageHeight = currentImage.getHeight(null) * 2;

			double adjustedImageWidth;
			double adjustedImageHeight;

			double aspectRatioOfImage = imageWidth / imageHeight;

			if (imageWidth <= panelWidth && imageHeight < panelHeight) {
				adjustedImageWidth = imageWidth;
				adjustedImageHeight = imageHeight;
			} else {
				adjustedImageWidth = Math.min(panelWidth, imageWidth * 2);
				adjustedImageHeight = panelWidth / aspectRatioOfImage;

				if (adjustedImageHeight >= panelHeight) {
					adjustedImageHeight = Math.min(panelHeight, imageHeight * 2);
					adjustedImageWidth = panelHeight * aspectRatioOfImage;
				}

			}
			size = new Dimension((int) adjustedImageWidth, (int) adjustedImageHeight);
		}

	}


}