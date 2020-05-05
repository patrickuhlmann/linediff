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

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class ReplaceCommandTest extends BaseTest {
    private final static String INPUT_FILENAME = "input.txt";
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private ReplaceCommand replaceCommand;

    @BeforeEach
    public void reinitialize() {
        replaceCommand = new ReplaceCommand();
    }

    @Test
    @DisplayName("should throw an exception when called without arguments")
    public void noArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> replaceCommand.execute(null));
    }

    @Test
    @DisplayName("should throw an exception when called with a wrong number of arguments")
    public void wrongNumberOfArguments() throws IOException {
        Path input = tempDir.resolve(INPUT_FILENAME);
        createFileOrFail(input);

        Assertions.assertThrows(IllegalArgumentException.class, () -> replaceCommand.execute(new String[]{input.toString(), "2", "3"}));

        Assertions.assertThrows(IllegalArgumentException.class, () -> replaceCommand.execute(new String[]{input.toString(), "2", "3", "4", "5"}));
    }


    @Test
    @DisplayName("should throw an exception if the input file does not exist")
    public void inputFileMustExist() {
        Path input = tempDir.resolve(INPUT_FILENAME); // NOPMD

        Assertions.assertThrows(FileNotFoundException.class,
                () -> replaceCommand.execute(new String[]{input.toString(), "2", "3", "4"}));
    }

    @Test
    @DisplayName("should throw an exception if the output file does already exist")
    public void outputFileMustntExist() throws IOException {
        Path input = tempDir.resolve(INPUT_FILENAME);
        createFileOrFail(input);

        Path output = tempDir.resolve("output.txt");
        createFileOrFail(output);

        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> replaceCommand.execute(new String[]{input.toString(), output.toString(), "3", "4"}));
    }

    @Test
    @DisplayName("regular replace")
    public void replaceNormal() throws Exception {
        List<String> inputLines = Arrays.asList("test 123 test", "main 123 main");
        List<String> outputLines = Arrays.asList("test test", "main main");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);
        Path output = tempDir.resolve("output.txt");

        replaceCommand.execute(new String[]{input.toString(), output.toString(), "\\s[0-9]*\\s", " "});

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
