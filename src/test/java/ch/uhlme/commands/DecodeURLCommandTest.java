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
import java.util.Collections;
import java.util.List;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class DecodeURLCommandTest extends BaseTest {
    private final static String INPUT_FILENAME = "input.txt";
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private DecodeURLCommand decodeURLCommand;

    @BeforeEach
    public void reinitialize() {
        decodeURLCommand = new DecodeURLCommand();
    }

    @Test
    @DisplayName("should throw an exception when called without arguments")
    public void noArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> decodeURLCommand.execute(null));
    }

    @Test
    @DisplayName("should throw an exception when called with a wrong number of arguments")
    public void wrongNumberOfArguments() throws IOException {
        Path input = tempDir.resolve(INPUT_FILENAME);
        createFileOrFail(input);

        Assertions.assertThrows(IllegalArgumentException.class, () -> decodeURLCommand.execute(new String[]{input.toString()}));

        Assertions.assertThrows(IllegalArgumentException.class, () -> decodeURLCommand.execute(new String[]{input.toString(), "1", "2"}));
    }


    @Test
    @DisplayName("should throw an exception if the input file does not exist")
    public void inputFileMustExist() {
        Path input = tempDir.resolve(INPUT_FILENAME); // NOPMD

        Assertions.assertThrows(FileNotFoundException.class,
                () -> decodeURLCommand.execute(new String[]{input.toString(), "2"}));
    }

    @Test
    @DisplayName("should throw an exception if the output file does already exist")
    public void outputFileMustntExist() throws IOException {
        Path input = tempDir.resolve(INPUT_FILENAME);
        createFileOrFail(input);

        Path output = tempDir.resolve("output.txt");
        createFileOrFail(output);

        Assertions.assertThrows(FileAlreadyExistsException.class,
                () -> decodeURLCommand.execute(new String[]{input.toString(), output.toString()}));
    }

    @Test
    @DisplayName("regular decode")
    public void replaceNormal() throws Exception {
        List<String> inputLines = Collections.singletonList("%C3%9Cber");
        List<String> outputLines = Collections.singletonList("Über");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);
        Path output = tempDir.resolve("output.txt");

        decodeURLCommand.execute(new String[]{input.toString(), output.toString()});

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
