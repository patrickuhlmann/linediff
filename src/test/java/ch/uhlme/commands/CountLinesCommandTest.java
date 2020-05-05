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
import java.util.regex.PatternSyntaxException;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class CountLinesCommandTest extends BaseTest {
    private final static String INPUT_FILENAME = "input.txt";
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;
    private CountLinesCommand countLinesCommand;

    @BeforeEach
    public void reinitialize() {
        countLinesCommand = new CountLinesCommand();
    }

    @Test
    @DisplayName("should throw an exception when called without arguments")
    public void noArguments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> countLinesCommand.execute(null));
    }

    @Test
    @DisplayName("should throw an exception when called with a wrong number of arguments")
    public void wrongNumberOfArguments() throws IOException {
        Path input = tempDir.resolve(INPUT_FILENAME);
        createFileOrFail(input);

        Assertions.assertThrows(IllegalArgumentException.class, () -> countLinesCommand.execute(new String[]{input.toString()}));

        Assertions.assertThrows(IllegalArgumentException.class, () -> countLinesCommand.execute(new String[]{input.toString(), "1", "2",}));
    }

    @Test
    @DisplayName("should throw an exception if the input file does not exist")
    public void inputFileMustExist() {
        Path input = tempDir.resolve(INPUT_FILENAME); // NOPMD

        Assertions.assertThrows(FileNotFoundException.class,
                () -> countLinesCommand.execute(new String[]{input.toString(), "2"}));
    }

    @Test
    @DisplayName("should throw an exception if the pattern is invalid")
    public void patternMustBeValid() throws IOException {
        Path input = tempDir.resolve(INPUT_FILENAME); // NOPMD
        createFileOrFail(input);

        Assertions.assertThrows(PatternSyntaxException.class,
                () -> countLinesCommand.execute(new String[]{input.toString(), "[[[[["}));
    }

    @Test
    @DisplayName("regular count")
    public void countNormal() throws Exception {
        List<String> inputLines = Arrays.asList("Test1", "abc", "Test2", "def", "Test3");

        Path input = tempDir.resolve(INPUT_FILENAME);
        Files.write(input, inputLines, StandardCharsets.UTF_8);

        countLinesCommand.execute(new String[]{input.toString(), "Test"});
        Assertions.assertEquals(3, countLinesCommand.getCounter());
    }
}
