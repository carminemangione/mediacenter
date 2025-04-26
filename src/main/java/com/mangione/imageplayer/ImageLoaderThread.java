package com.mangione.imageplayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageLoaderThread extends Thread {
	private ImageIcon imageIcon;
	private final String currentImageFile;

	ImageLoaderThread(String currentImageFile) {
		this.currentImageFile = currentImageFile;
		this.setName("ImageLoaderThread");
	}

	@Override
	public void run() {
		final String[] pieces = currentImageFile.split("\\.");
		final String suffix = pieces[pieces.length - 1];

		if (suffix.equals("gif")) {
			imageIcon = new ImageIcon(currentImageFile);
		}   else {
			try {
				BufferedImage image = ImageIO.read(new File(currentImageFile));
				imageIcon = new ImageIcon(image);
			} catch (Exception e) {
				//noinspection CallToPrintStackTrace
				e.printStackTrace();
			}
		}

	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}
}
