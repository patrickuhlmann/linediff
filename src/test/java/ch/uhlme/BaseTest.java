package ch.uhlme;

import ch.uhlme.utils.LogUtils;
import com.google.common.flogger.FluentLogger;
import org.junit.jupiter.api.Assertions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class BaseTest {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    static {
        try {
            LogUtils.initalizeLogging("logging.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createFileOrFail(Path file) throws IOException {
        if (!file.toFile().createNewFile()) {
            Assertions.fail(String.format("Unable to create file %s", file));
        }
    }

    public void deleteFileOrFail(Path file) {
        if (!file.toFile().delete()) {
            Assertions.fail(String.format("Unable to delete file %s", file));
        }
    }

    public void mkdirsOrFail(Path file) {
        if (!file.toFile().mkdirs()) {
            Assertions.fail(String.format("Unable to create directory %s", file));
        }
    }
}
