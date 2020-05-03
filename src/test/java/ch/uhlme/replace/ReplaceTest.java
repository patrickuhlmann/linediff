package ch.uhlme.replace;

import ch.uhlme.BaseTest;
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
import java.util.List;

public class ReplaceTest extends BaseTest {
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("should throw an exception when called with null arguments")
    public void noArguments() {
        Replace replace = new Replace();

        Assertions.assertThrows(NullPointerException.class, () -> replace.replace(null, Paths.get("file"), "", ""));
        Assertions.assertThrows(NullPointerException.class, () -> replace.replace(Paths.get("file"), Paths.get("file"), null, ""));
        Assertions.assertThrows(NullPointerException.class, () -> replace.replace(Paths.get("file2"), Paths.get("file"), "", null));
        Assertions.assertThrows(NullPointerException.class, () -> replace.replace(Paths.get("file2"), null, "", ""));
    }

    @Test
    @DisplayName("regular replace")
    public void replaceNormal() throws IOException {
        List<String> inputLines = Arrays.asList("test 123 test", "main 123 main");
        List<String> outputLines = Arrays.asList("test test", "main main");

        Path input = tempDir.resolve("file");
        Files.write(input, inputLines, StandardCharsets.UTF_8);
        Path output = tempDir.resolve("file_out");

        Replace replace = new Replace();

        replace.replace(input, output, "\\s[0-9]*\\s", " ");

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
