package ch.uhlme.sorting;

import ch.uhlme.utils.ByteCount;
import com.google.common.flogger.FluentLogger;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * based on Public Domain Code from // https://www.ashishsharma.me/2011/08/external-merge-sort.html
 */
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class ExternalSort {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private long splitSize = 20L * 1024L * 1024L;

  public ExternalSort() {}

  public ExternalSort(long splitSize) {
    this.splitSize = splitSize;
  }

  /**
   * Executes the sorting by line using the default java comparator. The used algorithm is External
   * Merge sort. Thus it will split the input file into 20 MB large temporary files. These will be
   * sorted and then merged in the end again.
   *
   * @param input file to sort
   * @param output sorted outputfile
   * @throws IOException if the input file cannot be read, any of the temporary files cannot be read
   *     or written too as well as the output file cannot be written.
   */
  public void sort(Path input, Path output) throws IOException {
    Objects.requireNonNull(input);
    Objects.requireNonNull(output);

    logger.atFine().log("sort with input %s, output %s", input, output);

    List<Path> l = splitFilesAndSort(input);
    mergeSortedFiles(l, output);
  }

  private List<Path> splitFilesAndSort(Path file) throws IOException {
    logger.atFine().log("Input file size %s", new ByteCount(Files.size(file)));

    List<Path> splitedFiles = new ArrayList<>();
    try (BufferedReader fbr = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
      List<String> lines = new ArrayList<>(); // NOPMD
      String currentLine = "";
      while (currentLine != null) {
        logger.atFine().atMostEvery(10, TimeUnit.SECONDS).log(
            "free memory: %s", new ByteCount(Runtime.getRuntime().freeMemory()));
        long currentSize = 0;
        currentLine = fbr.readLine();
        while ((currentSize < splitSize) && (currentLine != null)) {
          lines.add(currentLine);
          currentSize +=
              currentLine.length() * 2
                  + 40; // java uses 16 bits per character + 40 bytes of overhead (estimated)
          currentLine = fbr.readLine();
        }
        if (currentLine != null) {
          lines.add(currentLine);
        }
        if (!lines.isEmpty()) {
          splitedFiles.add(sortAndSave(lines));
        }
        lines.clear();
      }
    }

    return splitedFiles;
  }

  private Path sortAndSave(List<String> lines) throws IOException {
    logger.atFinest().log("%d lines to sort and save", lines.size());
    lines.sort(String::compareTo);
    File file = File.createTempFile("linediff_", "_flatfile");
    logger.atFine().log("Tempfile created %s", file);
    file.deleteOnExit();

    Files.write(file.toPath(), lines, StandardCharsets.UTF_8);

    return file.toPath();
  }

  private void mergeSortedFiles(List<Path> files, Path outputfile) throws IOException {
    logger.atFine().log("merge %d files", files.size());

    PriorityQueue<BinaryFileBuffer> pq =
        new PriorityQueue<>(files.size(), Comparator.comparing(BinaryFileBuffer::peek));

    for (Path f : files) {
      logger.atFinest().log("create reader for file %s", f);
      BinaryFileBuffer bfb = new BinaryFileBuffer(f);
      pq.add(bfb);
    }

    try (BufferedWriter fbw = Files.newBufferedWriter(outputfile, StandardCharsets.UTF_8)) {
      while (!pq.isEmpty()) {
        BinaryFileBuffer bfb = pq.poll();
        String r = bfb.pop();
        fbw.write(r);
        fbw.newLine();
        if (bfb.isEmpty()) {
          bfb.fbr.close();
          if (!Files.deleteIfExists(bfb.originalFile)) {
            logger.atWarning().log("Unable to delete temporary file %s", bfb.originalFile);
          }
        } else {
          pq.add(bfb);
        }
      }
    } finally {
      for (BinaryFileBuffer bfb : pq) {
        bfb.close();
      }
    }
  }

  @SuppressWarnings("PMD.BeanMembersShouldSerialize")
  static class BinaryFileBuffer {
    private final BufferedReader fbr;
    private final Path originalFile;
    private String cache;
    private boolean empty;

    public BinaryFileBuffer(Path f) throws IOException {
      originalFile = f;
      fbr = Files.newBufferedReader(f, StandardCharsets.UTF_8);
      reload();
    }

    public boolean isEmpty() {
      return empty;
    }

    private void reload() throws IOException {
      empty = (this.cache = fbr.readLine()) == null;
    }

    public void close() throws IOException {
      fbr.close();
    }

    public String peek() {
      return isEmpty() ? null : cache;
    }

    public String pop() throws IOException {
      String answer = peek();
      reload();
      return answer;
    }
  }
}
