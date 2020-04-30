import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import utils.FileUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ExternalSortTest extends BaseTest {
    @TempDir
    Path tempDir;
    Random random = new Random();

    @Test
    @DisplayName("should throw an exception when called with null arguments")
    public void noArguments() {
        ExternalSort ext = new ExternalSort();
        Path p = Paths.get("test");

        Assertions.assertThrows(NullPointerException.class, () -> ext.sort(p, null));
        Assertions.assertThrows(NullPointerException.class, () -> ext.sort(null, p));
    }

    @Test
    @DisplayName("sort small file")
    public void sortSmallFile() throws IOException {
        List<String> expected = Arrays.asList("a", "b", "c");

        Path f1 = tempDir.resolve("input.txt");
        Files.write(f1, Arrays.asList("c", "b", "a"), StandardCharsets.UTF_8);
        Path f2 = tempDir.resolve("output.txt");

        ExternalSort ext = new ExternalSort();
        ext.sort(f1, f2);

        verifyFile(f2, expected);
    }

    @Test
    @DisplayName("sort small file with duplicates")
    public void sortSmallFileWithDuplicates() throws IOException {
        List<String> expected = Arrays.asList("a", "b", "b", "b", "c");

        Path f1 = tempDir.resolve("input.txt");
        Files.write(f1, Arrays.asList("b", "c", "b", "b", "a"), StandardCharsets.UTF_8);
        Path f2 = tempDir.resolve("output.txt");

        ExternalSort ext = new ExternalSort();
        ext.sort(f1, f2);

        verifyFile(f2, expected);
    }

    @Test
    @DisplayName("sort small file with empty lines")
    public void sortSmallFileWithEmptyLines() throws IOException {
        List<String> expected = Arrays.asList("", "", "", "a", "b", "b", "b", "c");

        Path f1 = tempDir.resolve("input.txt");
        Files.write(f1, Arrays.asList("b", "c", "b", "b", "", "", "", "a"), StandardCharsets.UTF_8);
        Path f2 = tempDir.resolve("output.txt");

        ExternalSort ext = new ExternalSort();
        ext.sort(f1, f2);

        verifyFile(f2, expected);
    }

    @Test
    @DisplayName("sort file with splitting")
    public void sortFileWithSplitting() throws IOException {
        Path f1 = tempDir.resolve("input.txt");

        FileUtils.generateFileWithRandomLines(f1, 100);

        Path f2 = tempDir.resolve("output.txt");

        ExternalSort ext = new ExternalSort(700);
        ext.sort(f1, f2);
    }

    @Test
    @DisplayName("sort file with splitting smaller than lines")
    public void sortFileSplitSmallerThanLines() throws IOException {
        Path f1 = tempDir.resolve("input.txt");

        FileUtils.generateFileWithRandomLines(f1, 2);

        Path f2 = tempDir.resolve("output.txt");

        ExternalSort ext = new ExternalSort(300);
        ext.sort(f1, f2);
    }

    private void verifyFile(Path file, List<String> elements) throws IOException {
        List<String> lines = Files.readAllLines(file);
        Assertions.assertEquals(elements.size(), lines.size());

        for (int i=0; i<elements.size(); i++) {
            Assertions.assertEquals(elements.get(i), lines.get(i));
        }
    }
}
