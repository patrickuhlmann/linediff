package ch.uhlme.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileExists extends TypeSafeMatcher<Path> {
    public static Matcher<Path> fileExists() {
        return new FileExists();
    }

    @Override
    public boolean matchesSafely(Path path) {
        return Files.exists(path);
    }

    public void describeTo(Description description) {
        description.appendText("path exists");
    }

    protected void describeMismatchSafely(Path item, Description mismatchDescription) {
        mismatchDescription.appendText("path " + item + " did not exist");
    }
}
