package com.mangione.imageplayer;

import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileLoader {
	final static List<String> EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp");
	private final List<FileWithCompareString> availableImageFiles = Collections.synchronizedList(new ArrayList<>());
	private final Object fileWaiterObject = new Object();
	private final Random random = new Random();
	private int currentIndex = 0;

	FileLoader(File rootDirectory) {
		new Thread(() -> {recurseAndCollectFiles(rootDirectory);
			System.out.println("Loading thread finished");}).start();
	}

	synchronized FileWithCompareString getNextFile() {
		FileWithCompareString file;
		if (!availableImageFiles.isEmpty()) {
			file = availableImageFiles.get(currentIndex++);
			if (currentIndex == availableImageFiles.size()) {
				currentIndex = 0;
			}
		} else {
			try {
				synchronized (fileWaiterObject) {
					fileWaiterObject.wait();
				}
				file = availableImageFiles.get(currentIndex);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		return file;
	}

	synchronized FileWithCompareString getPreviousFile() {
		currentIndex = currentIndex > 0 ? currentIndex-- : availableImageFiles.size() - 1;
		return availableImageFiles.get(currentIndex);
	}

	int getCurrentIndex() {
		return currentIndex;
	}

	int getNumFilesLoaded() {
		return availableImageFiles.size();
	}

	void random() {
		currentIndex = random.nextInt(availableImageFiles.size());
	}

	private void recurseAndCollectFiles(File currentFile) {
		if (currentFile != null) {
			if (currentFile.isDirectory()) {
				final File[] files = currentFile.listFiles();
				if (files != null) {
					getImageFilesAndAddToList(files);
					System.out.printf("Loaded directory %s : %d%n", currentFile.getAbsolutePath(), files.length);
					System.out.println();
					Arrays.stream(files)
							.filter(File::isDirectory)
							.forEach(this::recurseAndCollectFiles);
				}
			}
		}
	}

	private void getImageFilesAndAddToList(File[] files) {
		final List<FileWithCompareString> imageFiles = findImageFilesSorted(files, file -> !file.isDirectory());
		synchronized (fileWaiterObject) {
			availableImageFiles.addAll(imageFiles);
			if (availableImageFiles.size() > 0) {
				currentIndex = random.nextInt(availableImageFiles.size());
				fileWaiterObject.notifyAll();
			}
		}
	}

	private List<FileWithCompareString> findImageFilesSorted(File[] files, Predicate<File> filePredicate) {
		return Arrays.stream(files)
				.filter(filePredicate)
				.map(this::getImageFile)
				.filter(Objects::nonNull)
				.sorted()
				.collect(Collectors.toList());

	}

	private FileWithCompareString getImageFile(File file) {
		FileWithCompareString fileWithCompareString = null;
		String name = file.getName();
		boolean imageFile = EXTENSIONS.stream().anyMatch(name::endsWith);
		//noinspection SpellCheckingInspection
		if (imageFile && !name.contains("WICThumbs") && !name.contains("DFTTH")) {
			fileWithCompareString = new FileWithCompareString(file);
		}
		return fileWithCompareString;
	}
}
