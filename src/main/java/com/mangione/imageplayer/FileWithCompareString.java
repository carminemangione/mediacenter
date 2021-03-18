package com.mangione.imageplayer;

import java.io.File;

import static com.mangione.imageplayer.FileLoader.EXTENSIONS;

public class FileWithCompareString implements Comparable<FileWithCompareString> {
      private final File file;
      private final String compareString;
      private Long number;

      FileWithCompareString(File file) {
          this.file = file;
          this.compareString = getLeadingStringStrippedOfCopyNumber(file.getAbsolutePath().toLowerCase());
      }


      String getLeadingStringStrippedOfCopyNumber(String name) {
          name = stripNameOfExtension(name);
          int locationOfVersionNumber = name.length() - 1;
          while (locationOfVersionNumber >= 1 && Character.isDigit(name.charAt(locationOfVersionNumber - 1))) {
              locationOfVersionNumber--;
          }

          final int lengthOfVersionNumber = name.length() - locationOfVersionNumber;
          final boolean isVersionReasonableNumber = lengthOfVersionNumber < 4
                  && lengthOfVersionNumber > 1;
          if (isVersionReasonableNumber) {
              number = Long.getLong(name.substring(locationOfVersionNumber));
          }

          return name.replace("-", "");
      }

      @Override
      public int compareTo(FileWithCompareString other) {
          try {
              int compareTo;
              if (compareString.equals(other.compareString)) {
                  compareTo = number != null && other.number != null ? number.compareTo(other.number) :
                          0;
              } else {
                  compareTo = compareString.compareTo(other.compareString);
              }
              return compareTo;
          } catch (Throwable e) {
              e.printStackTrace();
              return 0;
          }

      }

    private String stripNameOfExtension(String name) {
        for (String extension : EXTENSIONS) {
            name = name.replace(extension, "");
        }
        return name.replace(".", "");
    }

	public File getFile() {
		return file;
	}

    @Override
    public String toString() {
        return file.getAbsolutePath();
    }
}