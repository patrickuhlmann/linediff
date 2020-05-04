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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class ExternalSortCommandTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private ExternalSortCommand sortCommand;

    @BeforeEach
    public void reinitialize() {
        sortCommand = new ExternalSortCommand();
    }

    @Test
    @DisplayName("should throw an exception when called without arguments")
    public void noArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> sortCommand.run(null));
    }

    @Test
    @DisplayName("should throw an exception when called with a wrong number of arguments")
    public void wrongNumberOfArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> sortCommand.run(new String[]{"1"}));

        Assertions.assertThrows(IllegalArgumentException.class, () -> sortCommand.run(new String[]{"1", "2", "3"}));
    }

    @Test
    @DisplayName("should throw an exception if the input file does not exist")
    public void inputFilesMustExist() {
        Assertions.assertThrows(FileNotFoundException.class,
                () -> sortCommand.run(new String[]{"input.txt", "output.txt"}));
    }

    @Test
    @DisplayName("should throw an exception if the output file already exists")
    public void outputFileMustNotExist() throws IOException {
        Path input = tempDir.resolve("input.txt");
        createFileOrFail(input);

        Path output = tempDir.resolve("output.txt");
        createFileOrFail(output);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> sortCommand.run(new String[]{input.toString(), output.toString()}));
    }

    @Test
    @DisplayName("regular execution")
    public void regularExecutionWithSorting() throws IOException {
        List<String> inputLines = Arrays.asList("d", "a", "f");

        Path input = tempDir.resolve("input.txt");
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        Path output = tempDir.resolve("output.txt");

        Assertions.assertDoesNotThrow(() -> sortCommand.run(new String[]{input.toString(), output.toString()}));

        verifyFile(output, Arrays.asList("a", "d", "f"));
    }

    private void verifyFile(Path file, List<String> elements) throws IOException {
        List<String> lines = Files.readAllLines(file);
        Assertions.assertEquals(elements.size(), lines.size());

        for (int i = 0; i < elements.size(); i++) {
            Assertions.assertEquals(elements.get(i), lines.get(i));
        }
    }
}
