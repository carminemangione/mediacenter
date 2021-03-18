package com.mangione.imageplayer;

import com.luciad.imageio.webp.WebPReadParam;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
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
			ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
			WebPReadParam readParam = new WebPReadParam();
			readParam.setBypassFiltering(true);
			try {
				reader.setInput(new FileImageInputStream(new File(currentImageFile)));
				BufferedImage image = reader.read(0, readParam);
				imageIcon = new ImageIcon(image);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ImageIcon getImageIcon() {
		return imageIcon;
	}
}
