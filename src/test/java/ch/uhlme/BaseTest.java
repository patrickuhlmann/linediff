package ch.uhlme;

import ch.uhlme.utils.LogUtils;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;

public class BaseTest {
  static {
    try {
      LogUtils.initializeLogging("logging.properties");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void createFileOrFail(Path file) throws IOException {
    if (!file.toFile().createNewFile()) {
      Assertions.fail(String.format("Unable to create file %s", file));
    }
  }

  protected void deleteFileOrFail(Path file) {
    if (!file.toFile().delete()) {
      Assertions.fail(String.format("Unable to delete file %s", file));
    }
  }

  protected void mkdirsOrFail(Path file) {
    if (!file.toFile().exists() && !file.toFile().mkdirs()) {
      Assertions.fail(String.format("Unable to create directory %s", file));
    }
  }
}
