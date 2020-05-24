package ch.uhlme.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static ch.uhlme.matchers.FileExists.fileExists;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.DataflowAnomalyAnalysis"})
public class FileContentIs extends TypeSafeMatcher<Path> {
    final List<String> lines;

    public FileContentIs(String line) {
        this.lines = Collections.singletonList(line);
    }

    public FileContentIs(List<String> lines) {
        this.lines = lines;
    }

    public static Matcher<Path> fileContentIs(List<String> lines) {
        return new FileContentIs(lines);
    }

    public static Matcher<Path> fileContentIs(String line) {
        return new FileContentIs(line);
    }

    @Override
    public boolean matchesSafely(Path path) {
        assertThat(path, fileExists());

        try {
            List<String> linesInPath = Files.readAllLines(path);

            for (int i = 0; i < lines.size(); i++) {
                if (!linesInPath.get(i).equals(lines.get(i))) {
                    return false;
                }
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void describeTo(Description description) {
        description.appendText("content is lines " + lines);
    }

    protected void describeMismatchSafely(Path item, Description mismatchDescription) {
        try {
            mismatchDescription.appendText("path " + item + " content was " + Files.readAllLines(item));
        } catch (IOException e) {
            mismatchDescription.appendText(
                    "path " + item + " lead to an exception during the verification");
        }
    }
}
