import java.io.IOException;
import java.util.Objects;

public class LineDiff {
    private final InputFile firstInput;
    private final InputFile secondInput;
    private final OutputFolder output;

    public LineDiff(InputFile firstInput, InputFile secondInput, OutputFolder output) {
        Objects.requireNonNull(firstInput);
        Objects.requireNonNull(secondInput);
        Objects.requireNonNull(output);

        this.firstInput = firstInput;
        this.secondInput = secondInput;
        this.output = output;
    }

    public void process() throws IOException {
        String firstInputLine = this.firstInput.getNextUniqueLine();
        String secondInputLine = this.secondInput.getNextUniqueLine();

        while (firstInputLine != null && secondInputLine != null) {
            int result = firstInputLine.compareTo(secondInputLine);
            if (result < 0) {
                this.output.addOnlyFirstLine(firstInputLine);
                firstInputLine = this.firstInput.getNextUniqueLine();
            } else if (result == 0) {
                this.output.addBothLine(firstInputLine);
                firstInputLine = this.firstInput.getNextUniqueLine();
                secondInputLine = this.secondInput.getNextUniqueLine();
            } if (result > 0) {
                this.output.addOnlySecondLine(secondInputLine);
                secondInputLine = this.secondInput.getNextUniqueLine();
            }
        }

        while (firstInputLine != null) {
            this.output.addOnlyFirstLine(firstInputLine);
            firstInputLine = this.firstInput.getNextUniqueLine();
        }

        while (secondInputLine != null) {
            this.output.addOnlySecondLine(secondInputLine);
            secondInputLine = this.secondInput.getNextUniqueLine();
        }

        this.output.flush();
    }
}
