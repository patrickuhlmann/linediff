package ch.uhlme.diff;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.IOException;
import java.util.Objects;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2")
public class LineDiff {
  private final InputFile firstInput;
  private final InputFile secondInput;
  private final OutputFolder output;

  /**
   * Takes the first and second input file as well as an output folder where the result of the
   * LineDiff will be stored. To start the process, call the process() method.
   *
   * @param firstInput first file to process, note the file must be sorted
   * @param secondInput second file to process, note the file must be sorted
   * @param output folder where the three result files (lines contained in both, lines contained in
   *     the first, lines contained in the second) will be stored
   */
  public LineDiff(InputFile firstInput, InputFile secondInput, OutputFolder output) {
    Objects.requireNonNull(firstInput);
    Objects.requireNonNull(secondInput);
    Objects.requireNonNull(output);

    this.firstInput = firstInput;
    this.secondInput = secondInput;
    this.output = output;
  }

  /**
   * Executes the line diff process. It assumes that both input files are sorted and reads lines
   * from both files. It writes the lines two three output files. One file containing the lines that
   * the files have in common, another one containing the lines only found in the first file and yet
   * another one containing the lines found in both files.
   *
   * @throws IOException if an error happens during reading of the input files or writing of the
   *     output files
   */
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
      }
      if (result > 0) {
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
