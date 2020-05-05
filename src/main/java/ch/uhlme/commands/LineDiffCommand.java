package ch.uhlme.commands;

import ch.uhlme.diff.InputFile;
import ch.uhlme.diff.LineDiff;
import ch.uhlme.diff.OutputFolder;
import ch.uhlme.utils.FileUtils;
import ch.uhlme.utils.Tuple;
import com.google.common.flogger.FluentLogger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LineDiffCommand implements Command {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    @Override
    public void execute(String[] params) throws IOException {
        logger.atInfo().log("started with %s", params);

        Tuple<Path, Path> inputFiles = this.verifyParameters(params);
        this.runDiff(inputFiles.getFirst(), inputFiles.getSecond(), Paths.get(params[2]));

        logger.atInfo().log("Process finished successfully");
    }

    @Override
    public String getName() {
        return "linediff";
    }

    private Tuple<Path, Path> verifyParameters(String[] args) throws IOException {
        if (args == null || args.length != 3) {
            throw new IllegalArgumentException("Usage: linediff <firstfile> <secondfile> <outputfolder>");
        }

        logger.atInfo().log("Verifying parameters");

        if (Paths.get(args[0]).equals(Paths.get(args[1]))) {
            throw new IllegalArgumentException("Please specify two different input files");
        }

        Path firstPath = Paths.get(args[0]);
        Path secondPath = Paths.get(args[1]);

        if (!FileUtils.areLinesInFileSorted(firstPath) || !FileUtils.areLinesInFileSorted(secondPath)) {
            throw new IllegalArgumentException("One of the input files is unsorted. Please sort if first");
        }

        return new Tuple<>(firstPath, secondPath);
    }

    private void runDiff(Path firstPath, Path secondPath, Path outputFolderPath) throws IOException {
        try (
                InputFile firstFile = new InputFile(firstPath);
                InputFile secondFile = new InputFile(secondPath);
                OutputFolder outputFolder = new OutputFolder(outputFolderPath)
        ) {
            LineDiff diff = new LineDiff(firstFile, secondFile, outputFolder);
            diff.process();
        }
    }
}
