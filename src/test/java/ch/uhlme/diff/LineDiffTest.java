package ch.uhlme.diff;

import ch.uhlme.BaseTest;
import ch.uhlme.utils.FileUtils;
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
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class LineDiffTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("exception if any input or output is null")
    public void exceptionIfArgumentsAreNull() throws IOException {
        Path firstInput = tempDir.resolve("input1.txt");
        createFileOrFail(firstInput);
        Path secondInput = tempDir.resolve("input2.txt");
        createFileOrFail(secondInput);
        Path output = tempDir.resolve("output");

        Assertions.assertThrows(NullPointerException.class,
                () -> new LineDiff(null, new InputFile(secondInput), new OutputFolder(output)));
        FileUtils.deleteRecursive(output);


        Assertions.assertThrows(NullPointerException.class,
                () -> new LineDiff(new InputFile(firstInput), null, new OutputFolder(output)));
        FileUtils.deleteRecursive(output);

        Assertions.assertThrows(NullPointerException.class,
                () -> new LineDiff(new InputFile(firstInput), new InputFile(secondInput), null));

        Assertions.assertDoesNotThrow(() -> {
            new LineDiff(new InputFile(firstInput), new InputFile(secondInput), new OutputFolder(output));
        });
    }

    @Test
    @DisplayName("empty input files lead to empty output file")
    public void emptyInputEmptyOutput() throws IOException {
        runWithInputs(
                null,
                null);

        verifyBothFirstSecond(
                new LinkedList<>(),
                new LinkedList<>(),
                new LinkedList<>());
    }

    @Test
    @DisplayName("ch.uhlme.diff with empty second input")
    public void diffWithEmptySecondInput() throws IOException {
        runWithInputs(
                Arrays.asList("a", "b"),
                null);

        verifyBothFirstSecond(
                new LinkedList<>(),
                Arrays.asList("a", "b"),
                new LinkedList<>());
    }

    @Test
    @DisplayName("ch.uhlme.diff with empty first input")
    public void diffWithEmptyFirstInput() throws IOException {
        runWithInputs(
                null,
                Arrays.asList("a", "b"));

        verifyBothFirstSecond(
                new LinkedList<>(),
                new LinkedList<>(),
                Arrays.asList("a", "b"));
    }

    @Test
    @DisplayName("ch.uhlme.diff both inputs middle")
    public void diffBothInputsMiddle() throws IOException {
        runWithInputs(
                Arrays.asList("a", "b", "d"),
                Arrays.asList("a", "c", "d"));

        verifyBothFirstSecond(
                Arrays.asList("a", "d"),
                Collections.singletonList("b"),
                Collections.singletonList("c"));
    }

    @Test
    @DisplayName("ch.uhlme.diff both inputs start/end")
    public void diffBothInputsStartEnd() throws IOException {
        runWithInputs(
                Arrays.asList("a", "c", "d"),
                Arrays.asList("b", "c", "e"));

        verifyBothFirstSecond(
                Collections.singletonList("c"),
                Arrays.asList("a", "d"),
                Arrays.asList("b", "e"));
    }

    @Test
    @DisplayName("ch.uhlme.diff both inputs completely")
    public void diffBothInputsCompletely() throws IOException {
        runWithInputs(
                Arrays.asList("a", "c", "e"),
                Arrays.asList("b", "d", "f"));

        verifyBothFirstSecond(
                new LinkedList<>(),
                Arrays.asList("a", "c", "e"),
                Arrays.asList("b", "d", "f"));
    }

    @Test
    @DisplayName("ch.uhlme.diff first large")
    public void diffFirstLarge() throws IOException {
        runWithInputs(
                Arrays.asList("a", "b", "c", "d", "e", "f"),
                Arrays.asList("a", "b", "f"));

        verifyBothFirstSecond(
                Arrays.asList("a", "b", "f"),
                Arrays.asList("c", "d", "e"),
                new LinkedList<>());
    }

    @Test
    @DisplayName("ch.uhlme.diff second large")
    public void diffSecondLarge() throws IOException {
        runWithInputs(
                Arrays.asList("a", "b", "f"),
                Arrays.asList("a", "b", "c", "d", "e", "f"));

        verifyBothFirstSecond(
                Arrays.asList("a", "b", "f"),
                new LinkedList<>(),
                Arrays.asList("c", "d", "e"));
    }

    @Test
    @DisplayName("ch.uhlme.diff duplicates")
    public void diffDuplicates() throws IOException {
        runWithInputs(
                Arrays.asList("a", "b", "b", "b", "c"),
                Arrays.asList("a", "b", "c", "d", "e"));

        verifyBothFirstSecond(
                Arrays.asList("a", "b", "c"),
                new LinkedList<>(),
                Arrays.asList("d", "e"));
    }

    @Test
    @DisplayName("ch.uhlme.diff empty lines")
    public void diffEmptyLines() throws IOException {
        runWithInputs(
                Arrays.asList("", "", "", "a", "b", "b", "b", "c"),
                Arrays.asList("", "a", "b", "c", "d", "e"));

        verifyBothFirstSecond(
                Arrays.asList("", "a", "b", "c"),
                new LinkedList<>(),
                Arrays.asList("d", "e"));
    }

    private void runWithInputs(List<String> firstInput, List<String> secondInput) throws IOException {
        Path firstPath = tempDir.resolve("input1.txt");
        createFile(firstPath, firstInput);

        Path secondPath = tempDir.resolve("input2.txt");
        createFile(secondPath, secondInput);

        Path output = tempDir.resolve("output");

        LineDiff diff = new LineDiff(new InputFile(firstPath), new InputFile(secondPath), new OutputFolder(output));
        diff.process();
    }

    private void createFile(Path file, List<String> content) throws IOException {
        if (content == null || content.size() == 0) {
            createFileOrFail(file);
        } else {
            Files.write(file, content, StandardCharsets.UTF_8);
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

        for (int i=0; i<elements.size(); i++) {
            Assertions.assertEquals(elements.get(i), lines.get(i));
        }
    }
}