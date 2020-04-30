import com.google.common.flogger.FluentLogger;
import utils.FileUtils;
import utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    static {
        LogUtils.initalizeLogging("logging.properties");
    }

    public static void main(String[] args) throws Exception {
        Application app = new Application();
        app.run(args);
    }

    private void run(String[] args) throws Exception {
        logger.atInfo().log("started with %s", args);

        if (args.length != 3) {
            throw new IllegalArgumentException("Usage: <firstfile> <secondfile> <outputfolder>");
        }

        System.out.println("Verifying parameters");

        Path firstPath = sortInputFileIfNeeded(Paths.get(args[0]), Paths.get(args[2]));
        Path secondPath = sortInputFileIfNeeded(Paths.get(args[1]), Paths.get(args[2]));

        try (
                InputFile firstFile = new InputFile(firstPath);
                InputFile secondFile = new InputFile(secondPath);
                OutputFolder outputFolder = new OutputFolder(Paths.get(args[2]))
        ) {
            LineDiff diff = new LineDiff(firstFile, secondFile, outputFolder);
            diff.process();

            System.out.println("Process finished successfully");
        }
    }

    private static Path sortInputFileIfNeeded(Path input, Path output) throws IOException {
        if (!FileUtils.areLinesInFileSorted(input)) {
            System.out.println(String.format("Input file %s is unsorted, sorting...", input));
            output.toFile().mkdirs();
            ExternalSort sort = new ExternalSort();
            Path sortedInput = Paths.get(output.toAbsolutePath() + File.separator + "sorted_" + input.getFileName());
            if (sortedInput.toFile().exists()) {
                throw new IllegalArgumentException(String.format("Input file %s is unsorted and a sorted copy in the output folder can't be created, file already exists", input));
            }
            sort.sort(input, sortedInput);
            return sortedInput;
        } else {
            return input;
        }
    }
}
