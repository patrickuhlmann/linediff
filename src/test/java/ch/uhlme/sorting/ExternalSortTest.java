package ch.uhlme.sorting;

import ch.uhlme.BaseTest;
import ch.uhlme.utils.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static ch.uhlme.matchers.FileContentIs.fileContentIs;
import static ch.uhlme.preparation.PrepareFile.prepareFileWithLines;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
class ExternalSortTest extends BaseTest {
    @SuppressWarnings("unused")
    @TempDir
    Path tempDir;

    @Test
    @DisplayName("throw exception with null arguments")
    void givenNullArguments_thenThrowException() {
        ExternalSort ext = new ExternalSort();
        Path p = Paths.get("test");


        Assertions.assertThrows(NullPointerException.class,
                () -> ext.sort(p, null));

        Assertions.assertThrows(NullPointerException.class,
                () -> ext.sort(null, p));
    }

    @Test
    @DisplayName("sort small file")
    void givenSmallFile_thenSort() throws IOException {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("c", "b", "a"));
        Path output = tempDir.resolve("smalloutput.txt");


        ExternalSort ext = new ExternalSort();
        ext.sort(input, output);


        assertThat(output, fileContentIs(Arrays.asList("a", "b", "c")));
    }

    @Test
    @DisplayName("sort small file with duplicate lines")
    void givenSmallFileWithDuplicateLines_thenSort() throws IOException {
        Path input = prepareFileWithLines(tempDir, Arrays.asList("b", "c", "b", "b", "a"));
        Path output = tempDir.resolve("smallduplicate.txt");


        ExternalSort ext = new ExternalSort();
        ext.sort(input, output);


        assertThat(output, fileContentIs(Arrays.asList("a", "b", "b", "b", "c")));
    }

    @Test
    @DisplayName("sort small file with empty lines")
    void givenSmallFileWithEmptyLines_thenSort() throws IOException {
        Path f1 = prepareFileWithLines(tempDir, Arrays.asList("b", "c", "b", "b", "", "", "", "a"));
        Path f2 = tempDir.resolve("smallempty.txt");


        ExternalSort ext = new ExternalSort();
        ext.sort(f1, f2);


        assertThat(f2, fileContentIs(Arrays.asList("", "", "", "a", "b", "b", "b", "c")));
    }

    @Test
    @DisplayName("sort files that needs splitting")
    void givenFileThatNeedsSplitting_thenSort() throws IOException {
        Path input = tempDir.resolve("large.txt");
        FileUtils.generateFileWithRandomLines(input, 100);
        Path output = tempDir.resolve("output.txt");
        ExternalSort ext = new ExternalSort(700);


        Assertions.assertDoesNotThrow(() -> ext.sort(input, output));
    }
}
