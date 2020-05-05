package ch.uhlme.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LogUtils {
    private LogUtils() {
    }

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
