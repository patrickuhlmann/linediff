package ch.uhlme.commands;

import ch.uhlme.diff.InputFile;
import ch.uhlme.diff.LineDiff;
import ch.uhlme.diff.OutputFolder;
import ch.uhlme.sorting.ExternalSort;
import ch.uhlme.utils.FileUtils;
import ch.uhlme.utils.Tuple;
import com.google.common.flogger.FluentLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LineDiffCommand {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    public void run(String[] args) throws Exception {
        logger.atInfo().log("started with %s", args);

        Tuple<Path, Path> inputFiles = this.verifyParameters(args);
        this.runDiff(inputFiles.getFirst(), inputFiles.getSecond(), Paths.get(args[2]));

        logger.atInfo().log("Process finished successfully");
    }

    private Tuple<Path, Path> verifyParameters(String[] args) throws IOException {
        if (args == null || args.length != 3) {
            throw new IllegalArgumentException("Usage: linediff <firstfile> <secondfile> <outputfolder>");
        }

        logger.atInfo().log("Verifying parameters");

        Path firstPath = sortInputFileIfNeeded(Paths.get(args[0]), Paths.get(args[2]));
        Path secondPath = sortInputFileIfNeeded(Paths.get(args[1]), Paths.get(args[2]));

        return new Tuple<>(firstPath, secondPath);
    }

    private void runDiff(Path firstPath, Path secondPath, Path outputFolderPath) throws Exception {
        try (
                InputFile firstFile = new InputFile(firstPath);
                InputFile secondFile = new InputFile(secondPath);
                OutputFolder outputFolder = new OutputFolder(outputFolderPath)
        ) {
            LineDiff diff = new LineDiff(firstFile, secondFile, outputFolder);
            diff.process();
        }
    }

    private Path sortInputFileIfNeeded(Path input, Path output) throws IOException {
        if (!FileUtils.areLinesInFileSorted(input)) {
            System.out.println(String.format("Input file %s is unsorted, sorting...", input));
            if (!Files.exists(output) && Files.createDirectories(output) == null) {
                throw new IOException(String.format("Unable to create directory %s", output));
            }
            ExternalSort sort = new ExternalSort(); //NOPMD
            Path sortedInput = Paths.get(output.toAbsolutePath() + File.separator + "sorted_" + input.getFileName());
            if (Files.exists(sortedInput)) {
                throw new IllegalArgumentException(String.format("Input file %s is unsorted and a sorted copy in the output folder can't be created, file already exists", input));
            }
            sort.sort(input, sortedInput);
            return sortedInput;
        } else {
            return input;
        }
    }
}
