package ch.uhlme.sorting;// based on Public Domain Code from https://www.ashishsharma.me/2011/08/external-merge-sort.html

import ch.uhlme.utils.ByteCount;
import com.google.common.flogger.FluentLogger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ExternalSort {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();
    private long splitSize = 20 * 1024 * 1024;

    public ExternalSort() {

    }

    public ExternalSort(long splitSize) {
        this.splitSize = splitSize;
    }

    public void sort(Path input, Path output) throws IOException {
        Objects.requireNonNull(input);
        Objects.requireNonNull(output);

        logger.atFine().log("sort with input %s, output %s", input, output);

        List<Path> l = splitFilesAndSort(input);
        mergeSortedFiles(l, output);
    }

    private List<Path> splitFilesAndSort(Path file) throws IOException {
        logger.atFine().log("Input file size %s", new ByteCount(file.toFile().length()));

        List<Path> splitedFiles = new ArrayList<>();
        try (BufferedReader fbr = new BufferedReader(new FileReader(file.toFile()))) {
            List<String> lines = new ArrayList<>();
            String currentLine = "";
            try {
                while (currentLine != null) {
                    logger.atFine().atMostEvery(10, TimeUnit.SECONDS).log("free memory: %s", new ByteCount(Runtime.getRuntime().freeMemory()));
                    long currentSize = 0;
                    while ((currentSize < splitSize)
                            && ((currentLine = fbr.readLine()) != null)) {
                        lines.add(currentLine);
                        currentSize += currentLine.length() * 2 + 40; // java uses 16 bits per character + 40 bytes of overhead (estimated)
                    }
                    if (lines.size() > 0) {
                        splitedFiles.add(sortAndSave(lines));
                    }
                    lines.clear();
                }
            } catch (EOFException oef) {
                logger.atFine().log("EOF Exception");
                if (lines.size() > 0) {
                    splitedFiles.add(sortAndSave(lines));
                    lines.clear();
                }
            }
        }

        return splitedFiles;
    }

    private Path sortAndSave(List<String> lines) throws IOException {
        lines.sort(String::compareTo);
        File file = File.createTempFile("linediff_", "_flatfile");
        file.deleteOnExit();

        Files.write(file.toPath(), lines, StandardCharsets.UTF_8);

        return file.toPath();
    }

    private void mergeSortedFiles(List<Path> files, Path outputfile) throws IOException {
        logger.atFine().log("merge %d files", files.size());

        Comparator<String> cmp = String::compareTo;

        PriorityQueue<BinaryFileBuffer> pq = new PriorityQueue<>(files.size(),
                (i, j) -> cmp.compare(i.peek(), j.peek())
        );

        for (Path f : files) {
            logger.atFinest().log("create reader for file %s", f);
            BinaryFileBuffer bfb = new BinaryFileBuffer(f.toFile());
            pq.add(bfb);
        }

        try (BufferedWriter fbw = new BufferedWriter(new FileWriter(outputfile.toFile()))) {
            while (pq.size() > 0) {
                BinaryFileBuffer bfb = pq.poll();
                String r = bfb.pop();
                fbw.write(r);
                fbw.newLine();
                if (bfb.empty()) {
                    bfb.fbr.close();
                    if (!bfb.originalFile.delete()) {
                        logger.atWarning().log("Unable to delete temporary file %s", bfb.originalFile);
                    }
                } else {
                    pq.add(bfb);
                }
            }
        } finally {
            for (BinaryFileBuffer bfb : pq) bfb.close();
        }
    }

    static class BinaryFileBuffer {
        private final BufferedReader fbr;
        private final File originalFile;
        private String cache;
        private boolean empty;

        public BinaryFileBuffer(File f) throws IOException {
            originalFile = f;
            fbr = new BufferedReader(new FileReader(f));
            reload();
        }

        public boolean empty() {
            return empty;
        }

        private void reload() throws IOException {
            try {
                empty = (this.cache = fbr.readLine()) == null;
            } catch (EOFException oef) {
                empty = true;
                cache = null;
            }
        }

        public void close() throws IOException {
            fbr.close();
        }

        public String peek() {
            if (empty()) return null;
            return cache;
        }

        public String pop() throws IOException {
            String answer = peek();
            reload();
            return answer;
        }
    }
}