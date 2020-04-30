import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ApplicationTest extends BaseTest {
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("should throw an exception when called without arguments")
    public void noArguments() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Application.main(null);
        });
    }

    @Test
    @DisplayName("should throw an exception when called with a wrong number of arguments")
    public void wrongNumberOfArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Application.main(new String[] { "1" });
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Application.main(new String[] { "1", "2", "3", "4" });
        });
    }

    @Test
    @DisplayName("should throw an exception if one of the input files does not exist")
    public void inputFilesMustExist() throws IOException {
        Path firstInput = tempDir.resolve("first.txt");
        firstInput.toFile().createNewFile();

        Assertions.assertThrows(FileNotFoundException.class, () -> {
            Application.main(new String[] { "first.txt", "second.txt", "output.txt" });
        });

        Assertions.assertThrows(FileNotFoundException.class, () -> {
            Application.main(new String[] { firstInput.toString(), "second.txt", "output.txt" });
        });
    }

    @Test
    @DisplayName("should throw an exception if the output folder already exists")
    public void outputFileMustNotExist() throws IOException {
        Path firstInput = tempDir.resolve("input1.txt");
        firstInput.toFile().createNewFile();

        Path secondInput = tempDir.resolve("input2.txt");
        secondInput.toFile().createNewFile();

        Path outputFolder = tempDir.resolve("output");
        Path output = tempDir.resolve("output/both.txt");
        output.toFile().mkdirs();
        output.toFile().createNewFile();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Application.main(new String[] { firstInput.toString(), secondInput.toString(), outputFolder.toString() });
        });
    }

    @Test
    @DisplayName("regular execution")
    public void regularExecutionWithSorting() throws IOException {
        List<String> firstInputLines = Arrays.asList("a", "b", "c");
        List<String> secondInputLines = Arrays.asList("d", "a", "f");

        Path firstInput = tempDir.resolve("input1.txt");
        Files.write(firstInput, firstInputLines, StandardCharsets.UTF_8);

        Path secondInput = tempDir.resolve("input2.txt");
        Files.write(secondInput, secondInputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve("output");

        Assertions.assertDoesNotThrow(() -> {
            Application.main(new String[] { firstInput.toString(), secondInput.toString(), output.toString() });
        });

        verifyBothFirstSecond(
                Arrays.asList("a"),
                Arrays.asList("b", "c"),
                Arrays.asList("d", "f"));
    }

    private void verifyBothFirstSecond(List<String> elementsBoth, List<String> elementsFirst, List<String> elementsSecond) throws IOException {
        verifyFile("both.txt", elementsBoth);
        verifyFile("first_only.txt", elementsFirst);
        verifyFile("second_only.txt", elementsSecond);
    }

    private void verifyFile(String file, List<String> elements) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(tempDir.resolve("output").toString(), file));
        Assertions.assertEquals(elements.size(), lines.size());

        for (int i=0; i<elements.size(); i++) {
            Assertions.assertEquals(elements.get(i), lines.get(i));
        }
    }
}
