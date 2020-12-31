package com.mangione.imageplayer;

import com.luciad.imageio.webp.WebPReadParam;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImagePanel extends JPanel {
	private Dimension imageSize;
	private String currentImageFile;
	private final JLabel jLabel;


	ImagePanel(String currentImageFile) {
		setOpaque(true);
		setBackground(Color.DARK_GRAY);
		this.currentImageFile = currentImageFile;
		setLayout(new BorderLayout());

		jLabel = new JLabel(new ImageIcon(currentImageFile));
		jLabel.setOpaque(false);
		add(jLabel, BorderLayout.CENTER);
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				SwingUtilities.invokeLater(() -> {
					ImageIcon currentImage = getCurrentImage();
					if (currentImage != null) {
						jLabel.setIcon(currentImage);
						repaint();
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
		ImageIcon imageIcon = null;
		final String[] pieces = currentImageFile.split("\\.");
		ImageReader reader = ImageIO.getImageReadersBySuffix(pieces[pieces.length - 1]).next();
		WebPReadParam readParam = new WebPReadParam();
		readParam.setBypassFiltering(true);
		try {
			reader.setInput(new FileImageInputStream(new File(currentImageFile)));
			BufferedImage image = reader.read(0, readParam);
			imageIcon = new ImageIcon(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageIcon;
	}

	public synchronized void setImage(final String imageFileName) {
		currentImageFile = imageFileName;
		try {
			SwingUtilities.invokeLater(() -> {
				jLabel.setIcon(getCurrentImage());
				repaint();
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	public Dimension getMaximumSize() {
		return imageSize;
	}

	private class QuadDimension {
		private final Dimension size;

		QuadDimension(Image currentImage) {
			Dimension panelSize = getSize();

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