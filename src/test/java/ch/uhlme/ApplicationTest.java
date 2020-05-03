package ch.uhlme;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ApplicationTest extends BaseTest {
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("should throw an exception when called with null arguments")
    public void noArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                Application.main(null)
        );
    }

    @Test
    @DisplayName("should throw an exception when called with an unknown command")
    public void unknownCommand() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                Application.main(new String[]{"somecommand"})
        );
    }

    @Test
    @DisplayName("start linediffcommand")
    public void lineDiffCommand() throws IOException {
        List<String> firstInputLines = Arrays.asList("a", "b", "c");
        List<String> secondInputLines = Arrays.asList("d", "a", "f");

        Path firstInput = tempDir.resolve("input1.txt");
        Files.write(firstInput, firstInputLines, StandardCharsets.UTF_8);

        Path secondInput = tempDir.resolve("input2.txt");
        Files.write(secondInput, secondInputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve("output");

        Assertions.assertDoesNotThrow(
                () -> Application.main(new String[]{"linediff", firstInput.toString(), secondInput.toString(), output.toString()}));

        verifyBothFirstSecond(
                Collections.singletonList("a"),
                Arrays.asList("b", "c"),
                Arrays.asList("d", "f"));
    }

    @Test
    @DisplayName("start external sort")
    public void externalSortCommand() throws IOException {
        List<String> inputLines = Arrays.asList("d", "a", "f");

        Path input = tempDir.resolve("input.txt");
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve("output.txt");

        Assertions.assertDoesNotThrow(() ->
                Application.main(new String[]{"externalsort", input.toString(), output.toString()}));

        verifyFile(output, Arrays.asList("a", "d", "f"));
    }

    @Test
    @DisplayName("split execution")
    public void regularExecutionWithSplit() throws IOException {
        List<String> inputLines = Arrays.asList("a", "b", "c");

        Path input = tempDir.resolve("input.txt");
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        Assertions.assertDoesNotThrow(() -> Application.main(new String[]{"split", input.toString(), "2"}));

        Path output1 = tempDir.resolve("input_1.txt");
        verifyFile(output1, Arrays.asList("a", "b"));

        Path output2 = tempDir.resolve("input_2.txt");
        verifyFile(output2, Collections.singletonList("c"));
    }

    private void verifyFile(Path file, List<String> elements) throws IOException {
        List<String> lines = Files.readAllLines(file);
        Assertions.assertEquals(elements.size(), lines.size());

        for (int i = 0; i < elements.size(); i++) {
            Assertions.assertEquals(elements.get(i), lines.get(i));
        }
    }

    private void verifyBothFirstSecond(List<String> elementsBoth, List<String> elementsFirst, List<String> elementsSecond) throws IOException {
        verifyFile("both.txt", elementsBoth);
        verifyFile("first_only.txt", elementsFirst);
        verifyFile("second_only.txt", elementsSecond);
    }

    private void verifyFile(String file, List<String> elements) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(tempDir.resolve("output").toString(), file));
        Assertions.assertEquals(elements.size(), lines.size());

        for (int i = 0; i < elements.size(); i++) {
            Assertions.assertEquals(elements.get(i), lines.get(i));
        }
    }
}
