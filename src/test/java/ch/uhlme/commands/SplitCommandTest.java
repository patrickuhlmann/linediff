package ch.uhlme.commands;

import ch.uhlme.BaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class SplitCommandTest extends BaseTest {
    private final static String INPUT_FILENAME = "input.txt";
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private SplitCommand splitCommand;

    @BeforeEach
    public void reinitialize() {
        splitCommand = new SplitCommand();
    }

    @Test
    @DisplayName("should throw an exception when called without arguments")
    public void noArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> splitCommand.execute(null));
    }

    @Test
    @DisplayName("should throw an exception when called with a wrong number of arguments")
    public void wrongNumberOfArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> splitCommand.execute(new String[]{"1"}));

        Assertions.assertThrows(IllegalArgumentException.class, () -> splitCommand.execute(new String[]{"1", "2", "3"}));
    }

    @Test
    @DisplayName("should throw an exception if the input file does not exist")
    public void inputFileMustExist() {
        Assertions.assertThrows(FileNotFoundException.class,
                () -> splitCommand.execute(new String[]{INPUT_FILENAME, "10"}));
    }

    @Test
    @DisplayName("lines must be a positive integer")
    public void linesMustBePositiveInteger() throws IOException {
        Path input = tempDir.resolve(INPUT_FILENAME);
        createFileOrFail(input);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> splitCommand.execute(new String[]{input.toString(), "some"}));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> splitCommand.execute(new String[]{input.toString(), "-20"}));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> splitCommand.execute(new String[]{input.toString(), "0"}));
    }

    @Test
    @DisplayName("should throw an exception if the output file already exists")
    public void outputFileMustNotExist() throws IOException {
        List<String> inputLines = Arrays.asList("d", "a", "f");
        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve("input_1.txt");
        createFileOrFail(output);

        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> splitCommand.execute(new String[]{input.toString(), "20"}));
    }

    @Test
    @DisplayName("less lines than we split")
    public void lessLinesThanWeSplit() throws IOException {
        List<String> inputLines = Arrays.asList("d", "a", "f");
        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        Assertions.assertDoesNotThrow(
                () -> splitCommand.execute(new String[]{input.toString(), "5"}));

        Path output = tempDir.resolve("input_1.txt");

        verifyFile(output, Arrays.asList("d", "a", "f"));

        Path output2 = tempDir.resolve("input_2.txt");
        Assertions.assertFalse(output2.toFile().exists());
    }

    private void verifyFile(Path file, List<String> elements) throws IOException {
        List<String> lines = Files.readAllLines(file);
        Assertions.assertEquals(elements.size(), lines.size());

        for (int i = 0; i < elements.size(); i++) {
            Assertions.assertEquals(elements.get(i), lines.get(i));
        }
    }

    @Test
    @DisplayName("regular execution")
    public void regularExecutionWithSorting() throws IOException {
        List<String> inputLines = Arrays.asList("a", "b", "c", "d", "e");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        Assertions.assertDoesNotThrow(() -> splitCommand.execute(new String[]{input.toString(), "2"}));

        Path output1 = tempDir.resolve("input_1.txt");
        verifyFile(output1, Arrays.asList("a", "b"));

        Path output2 = tempDir.resolve("input_2.txt");
        verifyFile(output2, Arrays.asList("c", "d"));

        Path output3 = tempDir.resolve("input_3.txt");
        verifyFile(output3, Collections.singletonList("e"));
    }


    @Test
    @DisplayName("should throw an exception if a later output file already exists")
    public void laterOutputFileMustNotExist() throws IOException {
        List<String> inputLines = Arrays.asList("a", "b", "c", "d", "e");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve("input_3.txt");
        createFileOrFail(output);

        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> splitCommand.execute(new String[]{input.toString(), "2"}));
    }
}
