package ch.uhlme.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class LogUtilsTest {
  @Test
  @DisplayName("throws exception if the config file does not exist")
  void givenFileNotExist_throwException() {
    Assertions.assertThrows(
            FileNotFoundException.class, () -> LogUtils.initializeLogging("doesnotexist.properties"));
  }

  @Test
  @DisplayName("normal initialization")
  void regularException() {
    Assertions.assertDoesNotThrow(() -> LogUtils.initializeLogging("logging.properties"));
  }
}
