package ch.uhlme.commands;

import ch.uhlme.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class LineDiffCommandTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private LineDiffCommand diffCommand;
    private final static String INPUT_FILENAME = "input1.txt";
    private final static String INPUT2_FILENAME = "input2.txt";
    private final static String OUTPUT_FILENAME = "output";

    @BeforeEach
    public void reinitialize() {
        diffCommand = new LineDiffCommand();
    }

    @Test
    @DisplayName("should throw an exception when called without arguments")
    public void noArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> diffCommand.execute(null));
    }

    @Test
    @DisplayName("should throw an exception when called with a wrong number of arguments")
    public void wrongNumberOfArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> diffCommand.execute(new String[]{"1"}));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{"1", "2", "3", "4"}));
    }

    @Test
    @DisplayName("should throw an exception if one of the input files does not exist")
    public void inputFilesMustExist() throws IOException {
        Path firstInput = tempDir.resolve("first.txt");
        createFileOrFail(firstInput);

        Assertions.assertThrows(NoSuchFileException.class,
                () -> diffCommand.execute(new String[]{"first.txt", "second.txt", "output.txt"}));

        Assertions.assertThrows(NoSuchFileException.class,
                () -> diffCommand.execute(new String[]{firstInput.toString(), "second.txt", "output.txt"}));
    }

    @Test
    @DisplayName("should throw an exception if the output folder already exists")
    public void outputFileMustNotExist() throws IOException {
        Path firstInput = tempDir.resolve(INPUT_FILENAME);
        createFileOrFail(firstInput);

        Path secondInput = tempDir.resolve(INPUT2_FILENAME);
        createFileOrFail(secondInput);

        Path outputFolder = tempDir.resolve(OUTPUT_FILENAME);
        Path output = tempDir.resolve("output/both.txt");
        mkdirsOrFail(outputFolder);
        createFileOrFail(output);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{firstInput.toString(), secondInput.toString(), outputFolder.toString()}));
    }

    @Test
    @DisplayName("regular execution")
    public void regularExecutionWithSorting() throws IOException {
        List<String> firstInputLines = Arrays.asList("b", "c", "g");
        List<String> secondInputLines = Arrays.asList("d", "f", "g");

        Path firstInput = tempDir.resolve(INPUT_FILENAME);
        Files.write(firstInput, firstInputLines, StandardCharsets.UTF_8);

        Path secondInput = tempDir.resolve(INPUT2_FILENAME);
        Files.write(secondInput, secondInputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve(OUTPUT_FILENAME); // NOPMD

        Assertions.assertDoesNotThrow(
                () -> diffCommand.execute(new String[]{firstInput.toString(), secondInput.toString(), output.toString()}));

        verifyBothFirstSecond(
                Collections.singletonList("g"),
                Arrays.asList("b", "c"),
                Arrays.asList("d", "f"));
    }

    @Test
    @DisplayName("fail if first unsorted")
    public void failExecutionFirstUnsorted() throws IOException {
        List<String> firstInputLines = Arrays.asList("g", "b", "c");
        List<String> secondInputLines = Arrays.asList("a", "b", "c");

        Path firstInput = tempDir.resolve(INPUT_FILENAME);
        Files.write(firstInput, firstInputLines, StandardCharsets.UTF_8);

        Path secondInput = tempDir.resolve(INPUT2_FILENAME);
        Files.write(secondInput, secondInputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve(OUTPUT_FILENAME); // NOPMD

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{firstInput.toString(), secondInput.toString(), output.toString()}));
    }

    @Test
    @DisplayName("fail if second unsorted")
    public void failExecutionSecondUnsorted() throws IOException {
        List<String> firstInputLines = Arrays.asList("a", "b", "c");
        List<String> secondInputLines = Arrays.asList("h", "g", "f");

        Path firstInput = tempDir.resolve(INPUT_FILENAME);
        Files.write(firstInput, firstInputLines, StandardCharsets.UTF_8);

        Path secondInput = tempDir.resolve(INPUT2_FILENAME);
        Files.write(secondInput, secondInputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve(OUTPUT_FILENAME); // NOPMD

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{firstInput.toString(), secondInput.toString(), output.toString()}));
    }

    @Test
    @DisplayName("fail if executing with same input file")
    public void failIfSameInputFile() throws IOException {
        List<String> firstInputLines = Arrays.asList("g", "b", "c");
        List<String> secondInputLines = Arrays.asList("d", "g", "f");

        Path firstInput = tempDir.resolve(INPUT_FILENAME);
        Files.write(firstInput, firstInputLines, StandardCharsets.UTF_8);

        Path secondInput = tempDir.resolve("input1.txt");
        Files.write(secondInput, secondInputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve(OUTPUT_FILENAME); // NOPMD

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> diffCommand.execute(new String[]{firstInput.toString(), secondInput.toString(), output.toString()}));
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
