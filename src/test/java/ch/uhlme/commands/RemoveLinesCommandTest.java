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
import java.util.List;
import java.util.regex.PatternSyntaxException;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class RemoveLinesCommandTest extends BaseTest {
    private final static String INPUT_FILENAME = "input.txt";
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private RemoveLinesCommand removeLinesCommand;

    @BeforeEach
    public void reinitialize() {
        removeLinesCommand = new RemoveLinesCommand();
    }

    @Test
    @DisplayName("should throw an exception when called without arguments")
    public void noArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> removeLinesCommand.execute(null));
    }

    @Test
    @DisplayName("should throw an exception when called with a wrong number of arguments")
    public void wrongNumberOfArguments() throws IOException {
        Path input = tempDir.resolve(INPUT_FILENAME);
        createFileOrFail(input);

        Assertions.assertThrows(IllegalArgumentException.class, () -> removeLinesCommand.execute(new String[]{input.toString(), "2"}));

        Assertions.assertThrows(IllegalArgumentException.class, () -> removeLinesCommand.execute(new String[]{input.toString(), "2", "3", "4", "5"}));
    }


    @Test
    @DisplayName("should throw an exception if the input file does not exist")
    public void inputFileMustExist() {
        Path input = tempDir.resolve(INPUT_FILENAME); // NOPMD

        Assertions.assertThrows(FileNotFoundException.class,
                () -> removeLinesCommand.execute(new String[]{input.toString(), "2", "3"}));
    }

    @Test
    @DisplayName("should throw an exception if the output file does already exist")
    public void outputFileMustntExist() throws IOException {
        Path input = tempDir.resolve(INPUT_FILENAME);
        createFileOrFail(input);

        Path output = tempDir.resolve("output.txt");
        createFileOrFail(output);

        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> removeLinesCommand.execute(new String[]{input.toString(), output.toString(), "3"}));
    }

    @Test
    @DisplayName("should throw an exception if the pattern is invalid")
    public void patternMustBeValid() throws IOException {
        Path input = tempDir.resolve(INPUT_FILENAME); // NOPMD
        createFileOrFail(input);

        Path output = tempDir.resolve("output.txt"); // NOPMD

        Assertions.assertThrows(PatternSyntaxException.class,
                () -> removeLinesCommand.execute(new String[]{input.toString(), output.toString(), "[[[[["}));
    }

    @Test
    @DisplayName("regular remove lines")
    public void removeLines() throws Exception {
        List<String> inputLines = Arrays.asList("line1", "test", "line2", "", "line3");
        List<String> outputLines = Arrays.asList("line1", "line2", "", "line3");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);
        Path output = tempDir.resolve("output.txt");

        removeLinesCommand.execute(new String[]{input.toString(), output.toString(), "test"});

        verifyFile(output, outputLines);
    }

    private void verifyFile(Path file, List<String> elements) throws IOException {
        List<String> lines = Files.readAllLines(file);
        Assertions.assertEquals(elements.size(), lines.size());

        for (int i = 0; i < elements.size(); i++) {
            Assertions.assertEquals(elements.get(i), lines.get(i));
        }
    }
}
