package ch.uhlme.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LogUtils {
    private LogUtils() {
    }

    public static void initalizeLogging(String resource) throws IOException {
        try (InputStream stream = Thread.currentThread().getContextClassLoader().
                getResourceAsStream(resource)) {

            if (stream == null) {
                throw new FileNotFoundException(String.format("unable to find resource: %s", resource));
            }

            try {
                LogManager.getLogManager().readConfiguration(stream);
            } catch (IOException e) {
                System.out.println("Unable to load logging configuration.");
                e.printStackTrace();
            }
        }
    }
}
