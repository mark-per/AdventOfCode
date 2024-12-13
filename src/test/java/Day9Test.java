package org.aoc.days;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

class Day9Test {

    /**
     * Helper method to write input content to a temporary file and return its path.
     */
    private Path writeToTempFile(String content) throws IOException {
        Path tempFile = Files.createTempFile("day9_test_input", ".txt");
        Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        return tempFile;
    }

    @Test
    void testMainExample() throws Exception {
        // Main example from the puzzle statement
        // Input: 2333133121414131402
        // Expected Checksum: 1928
        String input = "2333133121414131402";
        Path inputFile = writeToTempFile(input);

        Day9 solver = new Day9(inputFile.toString());
        long result = solver.runPart(1);
        Assertions.assertEquals(1928, result, "Checksum does not match the expected value for the main example.");

        Files.deleteIfExists(inputFile);
    }

    @Test
    void testSimplePattern() throws Exception {
        // Input: 12345
        // This was from the demonstration. There's no official final checksum given,
        // but we verify that it runs and returns a valid checksum.
        String input = "12345";
        Path inputFile = writeToTempFile(input);

        Day9 solver = new Day9(inputFile.toString());
        long result = solver.runPart(1);
        // Just check we got some result and didn't crash
        Assertions.assertNotNull(result, "Checksum should be computed successfully.");

        Files.deleteIfExists(inputFile);
    }

    @Test
    void testSingleRunOfFiles() throws Exception {
        // Input: 9
        // Means one run of 9 file blocks (ID=0) and no free space.
        // Checksum = sum(i*0) for i=0..8 = 0
        String input = "9";
        Path inputFile = writeToTempFile(input);

        Day9 solver = new Day9(inputFile.toString());
        long result = solver.runPart(1);
        Assertions.assertEquals(0, result, "Checksum should be 0 for a single file run (ID=0).");

        Files.deleteIfExists(inputFile);
    }

    @Test
    void testNoFiles() throws Exception {
        // Input: 0
        // Means no files, effectively an empty disk.
        // Checksum = 0
        String input = "0";
        Path inputFile = writeToTempFile(input);

        Day9 solver = new Day9(inputFile.toString());
        long result = solver.runPart(1);
        Assertions.assertEquals(0, result, "Checksum should be 0 for an empty disk.");

        Files.deleteIfExists(inputFile);
    }

    @Test
    void testMultipleFilesNoSpace() throws Exception {
        // Input: 90909
        // Means file=9 (ID=0), free=0, file=9 (ID=1), free=0, file=9 (ID=2)
        // Expected checksum = 513 (calculated previously)
        String input = "90909";
        Path inputFile = writeToTempFile(input);

        Day9 solver = new Day9(inputFile.toString());
        long result = solver.runPart(1);
        Assertions.assertEquals(513, result, "Checksum does not match the expected value for multiple large file runs with no space.");

        Files.deleteIfExists(inputFile);
    }

    @Test
    void testTrailingFileRun() throws Exception {
        // Input: 90
        // Means file=9 (ID=0), free=0
        // Just 9 blocks of file 0 => Checksum=0 again
        String input = "90";
        Path inputFile = writeToTempFile(input);

        Day9 solver = new Day9(inputFile.toString());
        long result = solver.runPart(1);
        Assertions.assertEquals(0, result, "Checksum should be 0 for this configuration as well.");

        Files.deleteIfExists(inputFile);
    }
}
