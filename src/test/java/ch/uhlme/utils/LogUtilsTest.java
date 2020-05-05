package ch.uhlme.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class LogUtilsTest {
    @Test
    @DisplayName("exception if initilaized with non-existant file")
    public void nonExistantFile() {
        Assertions.assertThrows(FileNotFoundException.class, () -> LogUtils.initializeLogging("doesnotexist.properties"));
    }

    @Test
    @DisplayName("no exception if initilaized with existant file")
    public void properFile() {
        Assertions.assertDoesNotThrow(() -> LogUtils.initializeLogging("logging.properties"));
    }
}
