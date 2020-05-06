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

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class ApplicationTest extends BaseTest {
    private static final String INPUT_FILENAME = "input.txt";
    private static final String OUTPUT_FILENAME = "output.txt";
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("should throw an exception when called with null or no arguments")
    public void noArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                Application.main(null)
        );

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                Application.main(new String[]{})
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
        List<String> secondInputLines = Arrays.asList("a", "d", "f");

        Path firstInput = tempDir.resolve("input1.txt");
        Files.write(firstInput, firstInputLines, StandardCharsets.UTF_8);

        Path secondInput = tempDir.resolve("input2.txt");
        Files.write(secondInput, secondInputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve("output"); // NOPMD

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

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve(OUTPUT_FILENAME);

        Assertions.assertDoesNotThrow(() ->
                Application.main(new String[]{"externalsort", input.toString(), output.toString()}));

        verifyFile(output, Arrays.asList("a", "d", "f"));
    }

    @Test
    @DisplayName("split execution")
    public void regularExecutionWithSplit() throws IOException {
        List<String> inputLines = Arrays.asList("a", "b", "c");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        Assertions.assertDoesNotThrow(() -> Application.main(new String[]{"split", input.toString(), "2"}));

        Path output1 = tempDir.resolve("input_1.txt");
        verifyFile(output1, Arrays.asList("a", "b"));

        Path output2 = tempDir.resolve("input_2.txt");
        verifyFile(output2, Collections.singletonList("c"));
    }

    @Test
    @DisplayName("replace")
    public void replaceNormal() throws Exception {
        List<String> inputLines = Arrays.asList("test 123 test", "main 123 main");
        List<String> outputLines = Arrays.asList("test test", "main main");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);
        Path output = tempDir.resolve(OUTPUT_FILENAME);

        Application.main(new String[]{"replace", input.toString(), output.toString(), "\\s[0-9]*\\s", " "});

        verifyFile(output, outputLines);
    }

    @Test
    @DisplayName("url decode")
    public void urlDecode() throws Exception {
        List<String> inputLines = Collections.singletonList("%C3%9Cber");
        List<String> outputLines = Collections.singletonList("Über");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);
        Path output = tempDir.resolve(OUTPUT_FILENAME);

        Application.main(new String[]{"decodeurl", input.toString(), output.toString()});

        verifyFile(output, outputLines);
    }

    @Test
    @DisplayName("regular count")
    public void countNormal() throws Exception {
        List<String> inputLines = Arrays.asList("Test1", "abc", "Test2", "def", "Test3");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        Assertions.assertDoesNotThrow(() -> Application.main(new String[]{"countlines", input.toString(), "Test"}));
    }

    @Test
    @DisplayName("regular remove lines")
    public void removeLinesNormal() throws Exception {
        List<String> inputLines = Arrays.asList("line1", "test", "line2", "", "line3");
        List<String> outputLines = Arrays.asList("line1", "line2", "", "line3");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);
        Path output = tempDir.resolve(OUTPUT_FILENAME);

        Application.main(new String[]{"removelines", input.toString(), output.toString(), "test"});

        verifyFile(output, outputLines);
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
