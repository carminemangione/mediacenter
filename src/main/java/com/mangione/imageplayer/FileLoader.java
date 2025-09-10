package com.mangione.imageplayer;

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

	FileLoader(File rootDirectory) throws IOException {
		File loadedFile = new File(rootDirectory, "loadedDirectories.json");
		if (loadedFile.exists()) {
			String directoriesJSON = FileUtils.readFileToString(loadedFile, "UTF-8");;
			loadedDirectories = Collections.synchronizedSet(
					new Gson().fromJson(directoriesJSON,  new TypeToken<Set<String>>(){}.getType()));
			File imageFile = new File(rootDirectory, IMAGE_FILE_PATHS);
			String imageFileJSON = FileUtils.readFileToString(imageFile, "UTF-8");
			List<String> imageFiles = new Gson().fromJson(imageFileJSON, new TypeToken<List<String>>(){}.getType());

			availableImageFiles = Collections.synchronizedList(
					imageFiles.stream()
							.map(File::new)
							.map(FileWithCompareString::new)
							.collect(Collectors.toList()));
		} else {
			loadedDirectories = Collections.synchronizedSet(new HashSet<>());
			availableImageFiles = Collections.synchronizedList(new ArrayList<>());
			final Thread thread = new Thread(() -> {
				recurseAndCollectFiles(rootDirectory);
				System.out.println("Loading thread finished");
				saveDirectoriesAndFilePaths(rootDirectory, loadedFile);
			});
			thread.setName("FileLoaderThread");
			thread.start();
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
					//noinspection WaitWhileHoldingTwoLocks
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
		currentIndex = currentIndex > 0 ? currentIndex - 1 : availableImageFiles.size() - 1;
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
		String directoryPath = currentFile.getPath();
		if (currentFile.isDirectory() && !loadedDirectories.contains(directoryPath)) {
			loadedDirectories.add(directoryPath);
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
		//noinspection SpellCheckingInspection
		if (imageFile && !name.contains("WICThumbs") && !name.contains("DFTTH")) {
			fileWithCompareString = new FileWithCompareString(file);
		}
		return fileWithCompareString;
	}
}
