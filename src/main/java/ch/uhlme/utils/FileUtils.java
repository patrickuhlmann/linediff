package ch.uhlme.utils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;

public class FileUtils {
    private static final Random random = new Random();

    public static boolean deleteRecursive(Path path) {
        Objects.requireNonNull(path);

        File[] flist = path.toFile().listFiles();
        if (flist != null && flist.length > 0) {
            for (File f : flist) {
                if (!deleteRecursive(f.toPath())) {
                    return false;
                }
            }
        }

        return path.toFile().delete();
    }

    public static boolean areLinesInFileSorted(Path path) throws IOException {
        Objects.requireNonNull(path);

        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String previousLine = "";
            String currentLine = br.readLine();
            while (currentLine != null) {
                if (previousLine.compareTo(currentLine) > 0) {
                    return false;
                }

                previousLine = currentLine;
                currentLine = br.readLine();
            }
        }

        return true;
    }

    public static void generateFileWithRandomLines(Path path, long lines) throws IOException {
        Objects.requireNonNull(path);

        if (lines < 0) {
            throw new IllegalArgumentException("lines must be positive");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path.toFile()))) {
            for (int i=0; i<lines; i++) {
                bw.write(randomLine());
                bw.newLine();
            }
            bw.flush();
        }
    }

    private static String randomLine() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 100;

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static Path getPathWithSuffixInFilename(Path path, String suffix) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(suffix);

        int fileExtensionPosition = path.toString().lastIndexOf(".");

        String filename;
        if (fileExtensionPosition > -1) {
            filename = path.toString().substring(0, fileExtensionPosition);
            filename += suffix;
            filename += path.toString().substring(fileExtensionPosition);
        } else {
            filename = path.toString() + suffix;
        }
        return Paths.get(filename);
    }

}
