import com.google.common.flogger.FluentLogger;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.logging.LogManager;

public class BaseTest {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    static {
        InputStream stream = Application.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
            logger.atInfo().log("Loaded logging config");
        } catch (IOException e) {
            System.out.println("Unable to load logging configuration. Exiting.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
