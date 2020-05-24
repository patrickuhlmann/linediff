package ch.uhlme.commands;

import ch.uhlme.utils.Tuple;
import com.google.common.flogger.FluentLogger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DecodeUrlCommand implements Command {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();

  @Override
  public void execute(String[] params) throws IOException {
    logger.atInfo().log("started with %s", params);

    Tuple<Path, Path> arguments = verifyParameters(params);

    decode(arguments.getFirst(), arguments.getSecond());

    logger.atInfo().log("Process finished successfully");
  }

  @Override
  public String getName() {
    return "decodeurl";
  }

  private Tuple<Path, Path> verifyParameters(String[] args)
      throws FileNotFoundException, FileAlreadyExistsException {
    if (args == null || args.length != 2) {
      throw new IllegalArgumentException("usage: decodeurl <input> <output>");
    }

    Path input = Paths.get(args[0]);
    if (!Files.exists(input)) {
      throw new FileNotFoundException(String.format("the input file %s can't be found", input));
    }

    Path output = Paths.get(args[1]);
    if (Files.exists(output)) {
      throw new FileAlreadyExistsException(
          String.format("the output file %s mustn't exist", output));
    }

    return new Tuple<>(input, output);
  }

  private void decode(Path input, Path output) throws IOException {

    try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8);
        BufferedWriter writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {

      String currentLine = reader.readLine();
      while (currentLine != null) {
        String replaced = URLDecoder.decode(currentLine, StandardCharsets.UTF_8.name());
        writer.write(replaced);
        writer.newLine();
        currentLine = reader.readLine();
      }
    }
  }
}
