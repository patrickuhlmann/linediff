package ch.uhlme;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static ch.uhlme.matchers.FileContentIs.fileContentIs;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class ApplicationTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("throw an exception when called with null or no arguments")
    void givenNullOrNoArgument_thenThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Application.main(null)
        );

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Application.main(new String[]{})
        );
    }

    @Test
    @DisplayName("throw an exception when called with an unknown command")
    void givenUnknownCommand_thenThrowException() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Application.main(new String[]{"somecommand"})
        );
    }

    @Test
    @DisplayName("execute the linediff command")
    void givenLineDiff_thenExecute() throws Exception {
        String firstInput = prepareFileWithLines(tempDir, Arrays.asList("a", "b", "c")).toString();
        String secondInput = prepareFileWithLines(tempDir, Arrays.asList("a", "d", "f")).toString();
        Path output = tempDir.resolve("output");


        Application.main(new String[]{"linediff", firstInput, secondInput, output.toString()});


        assertThat(Paths.get(output.toString(), "both.txt"), fileContentIs("a"));
        assertThat(Paths.get(output.toString(), "first_only.txt"), fileContentIs(Arrays.asList("b", "c")));
        assertThat(Paths.get(output.toString(), "second_only.txt"), fileContentIs(Arrays.asList("d", "f")));
    }

    @Test
    @DisplayName("execute external sort command")
    void givenExternalSort_thenExecute() throws Exception {
        String input = prepareFileWithLines(tempDir, Arrays.asList("d", "a", "f")).toString();
        Path output = tempDir.resolve("externalsort.txt");


        Application.main(new String[]{"externalsort", input, output.toString()});


        assertThat(output, fileContentIs(Arrays.asList("a", "d", "f")));
    }

    @Test
    @DisplayName("execute split command")
    void givenSplit_thenExecute() throws Exception {
        Path input = prepareFileWithLines(tempDir, "input.txt", Arrays.asList("a", "b", "c"));
        Path output1 = tempDir.resolve("input_1.txt");
        Path output2 = tempDir.resolve("input_2.txt");


        Application.main(new String[]{"split", input.toString(), "2"});


        assertThat(output1, fileContentIs(Arrays.asList("a", "b")));
        assertThat(output2, fileContentIs("c"));
    }

    @Test
    @DisplayName("execute replace command")
    void givenReplace_thenExecute() throws Exception {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("test 123 test", "main 123 main"));
        Path output = tempDir.resolve("replace.txt");


        Application.main(new String[]{"replace", input.toString(), output.toString(), "\\s[0-9]*\\s", " "});


        assertThat(output, fileContentIs(Arrays.asList("test test", "main main")));
    }

    @Test
    @DisplayName("execute url decode command")
    void givenUrlDecode_thenExecute() throws Exception {
        Path input = prepareFileWithLines(tempDir, Collections.singletonList("%C3%9Cber"));
        Path output = tempDir.resolve("urldecode.txt");


        Application.main(new String[]{"decodeurl", input.toString(), output.toString()});


        assertThat(output, fileContentIs(Collections.singletonList("Über")));
    }

    @Test
    @DisplayName("execute count")
    void givenCount_thenExecute() throws Exception {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("Test1", "abc", "Test2", "def", "Test3"));

        Application.main(new String[]{"countlines", input.toString(), "Test"});
    }

    @Test
    @DisplayName("execute remove lines")
    void givenRemoveLines_thenExecute() throws Exception {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("line1", "test", "line2", "", "line3"));
        Path output = tempDir.resolve("removelines.txt");


        Application.main(new String[]{"removelines", input.toString(), output.toString(), "test"});


        assertThat(output, fileContentIs(Arrays.asList("line1", "line2", "", "line3")));
    }
}
