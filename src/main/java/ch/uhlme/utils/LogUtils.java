package ch.uhlme.utils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LogUtils {
    private LogUtils() {
    }

    @SuppressFBWarnings(
            value = "RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE",
            justification = "Known Bug: https://github.com/spotbugs/spotbugs/issues/259")
    public static void initializeLogging(String resource) throws IOException {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().
                getResourceAsStream(resource)) {

            if (stream == null) {
                throw new FileNotFoundException(String.format("unable to find resource: %s", resource));
            }

            LogManager.getLogManager().readConfiguration(stream);
        }
    }
}
