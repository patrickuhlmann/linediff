package ch.uhlme.matchers;

import java.nio.file.Files;
import java.nio.file.Path;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class FileNotExists extends TypeSafeMatcher<Path> {
  public static Matcher<Path> fileNotExists() {
    return new FileNotExists();
  }

  @Override
  public boolean matchesSafely(Path path) {
    return !Files.exists(path);
  }

  public void describeTo(Description description) {
    description.appendText("path does not exists");
  }

  protected void describeMismatchSafely(Path item, Description mismatchDescription) {
    mismatchDescription.appendText("path " + item + " exist");
  }
}
