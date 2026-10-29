package com.mangione.imageplayer.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileLoader {
	final static List<String> EXTENSIONS = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp", ".mp4");
	public static final String IMAGE_FILE_PATHS = "imageFilePaths.json";
	private final List<FileWithCompareString> availableImageFiles;
	private final Set<String> loadedDirectories;
	private final Object fileWaiterObject = new Object();
	private final Random random = new Random();
	private int currentIndex = 0;
	private int currentNumberOfFiles;
	private FileWithCompareString currentFile;

	public FileLoader(File rootDirectory) throws IOException {
		File loadedFile = new File(rootDirectory, "loadedDirectories.json");
		List<String> imageFiles;
		if (loadedFile.exists()) {
			String directoriesJSON = FileUtils.readFileToString(loadedFile, "UTF-8");
			loadedDirectories = Collections.synchronizedSet(
					new Gson().fromJson(directoriesJSON, new TypeToken<Set<String>>() {
					}.getType()));
			File imageFile = new File(rootDirectory, IMAGE_FILE_PATHS);
			String imageFileJSON = FileUtils.readFileToString(imageFile, "UTF-8");
			imageFiles = new Gson().fromJson(imageFileJSON, new TypeToken<List<String>>() {
			}.getType());

			availableImageFiles = Collections.synchronizedList(
					imageFiles.stream()
							.map(File::new)
							.map(FileWithCompareString::new)
							.collect(Collectors.toList()));
			random();
		} else {
			loadedDirectories = Collections.synchronizedSet(new HashSet<>());
			availableImageFiles = Collections.synchronizedList(new ArrayList<>());
			imageFiles = Collections.synchronizedList(new ArrayList<>());
		}
		final Thread thread = new Thread(() -> {
			processFilesInRootDirectory(rootDirectory, imageFiles);

			System.out.println("Loading thread finished");
			if (rootDirectory.isDirectory()) {
				saveDirectoriesAndFilePaths(rootDirectory, loadedFile);

			}
		});
		thread.setName("FileLoaderThread");
		thread.start();
	}

	private void processFilesInRootDirectory(File rootDirectory, List<String> imageFiles) {
		File[] files = rootDirectory.listFiles();
		if (files != null) {
			getImageFilesAndAddToList(Arrays.stream(files)
					.filter(File::isFile)
					.filter(file -> !imageFiles.contains(file.getAbsolutePath()))
					.toArray(File[]::new));
			for (File file : files) {
				if (file.isDirectory() && !loadedDirectories.contains(file.getPath())) {
					recurseAndCollectFiles(file);
				}
			}
		}
	}

	private void saveDirectoriesAndFilePaths(File rootDirectory, File loadedFile) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(loadedFile));
			bw.write(new Gson().toJson(loadedDirectories));
			bw.flush();
			bw.close();
			File file = new File(rootDirectory, IMAGE_FILE_PATHS);
			bw = new BufferedWriter(new FileWriter(file));
			List<String> imagePaths = availableImageFiles.stream()
					.map(FileWithCompareString::getFile)
					.map(File::getAbsolutePath)
					.collect(Collectors.toList());
			String imageFiles = new Gson().toJson(imagePaths);
			bw.write(imageFiles);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void getNextFile() {
		if (currentNumberOfFiles < availableImageFiles.size()) {
			currentIndex = random.nextInt(availableImageFiles.size());
			currentNumberOfFiles = availableImageFiles.size();
		}
		FileWithCompareString file;
		if (!availableImageFiles.isEmpty()) {
			currentFile = availableImageFiles.get(currentIndex++);
			file = currentFile;
			if (currentIndex == availableImageFiles.size()) {
				currentIndex = 0;
			}
		} else {
			try {
				synchronized (fileWaiterObject) {
					while (availableImageFiles.isEmpty()) {
						//noinspection WaitWhileHoldingTwoLocks
						fileWaiterObject.wait();
					}
				}
				currentFile = availableImageFiles.get(currentIndex);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public synchronized void getPreviousFile() {
		currentIndex = currentIndex > 0 ? currentIndex - 1 : availableImageFiles.size() - 1;
		currentFile = availableImageFiles.get(currentIndex);
	}

	public synchronized FileWithCompareString getCurrentFile() {
		return currentFile;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public int getNumFilesLoaded() {
		return availableImageFiles.size();
	}

	public void random() {
		currentIndex = random.nextInt(availableImageFiles.size());
	}

	private void recurseAndCollectFiles(File currentFile) {
		String directoryPath = currentFile.getPath();
		if (currentFile.isDirectory()) {
			System.out.printf("Loading directory %s", currentFile.getAbsolutePath());
			System.out.println();
			final File[] files = currentFile.listFiles();
			if (files != null) {
				if (!loadedDirectories.contains(directoryPath)) {
					loadedDirectories.add(directoryPath);
					getImageFilesAndAddToList(files);
				}
				System.out.printf("Loaded directory %s : %d%n", currentFile.getAbsolutePath(), files.length);
				System.out.println();
				Arrays.stream(files)
						.filter(File::isDirectory)
						.forEach(this::recurseAndCollectFiles);
			}

		}
	}


	private void getImageFilesAndAddToList(File[] files) {
		final List<FileWithCompareString> imageFiles = findImageFilesSorted(files, file -> !file.isDirectory());
		synchronized (fileWaiterObject) {
			availableImageFiles.addAll(imageFiles);
			if (!availableImageFiles.isEmpty()) {
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
		if (imageFile && !name.contains("WICThumbs") && !name.contains("DFTTH")) {
			fileWithCompareString = new FileWithCompareString(file);
		}
		return fileWithCompareString;
	}
}
