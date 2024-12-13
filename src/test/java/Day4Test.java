package org.aoc.days;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day4Test {

    @Test
    public void testWordSearchOccurrences() throws IOException {
        // Create a temporary test file with the given grid
        File tempFile = File.createTempFile("day4_test", ".txt");
        tempFile.deleteOnExit();

        String grid = "MMMSXXMASM\n" +
                "MSAMXMSMSA\n" +
                "AMXSXMAAMM\n" +
                "MSAMASMSMX\n" +
                "XMASAMXAMM\n" +
                "XXAMMXXAMA\n" +
                "SMSMSASXSS\n" +
                "SAXAMASAAA\n" +
                "MAMMMXMMMM\n" +
                "MXMXAXMASX\n";

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(grid);
        }

        // Instantiate Day4 with the test file
        Day4 day4 = new Day4(tempFile.getAbsolutePath());

        // Run Part 1 and capture the output
        int occurrences = day4.part1();

        // Assert the expected result
        assertEquals(18, occurrences, "The word XMAS should occur 18 times in the given grid.");
    }

    @Test
    public void testXMASPatternOccurrences() throws IOException {
        // Create a temporary test file with the given grid
        File tempFile = File.createTempFile("day4_test_part2", ".txt");
        tempFile.deleteOnExit();

        String grid = ".M.S......\n" +
                "..A..MSMS.\n" +
                ".M.S.MAA..\n" +
                "..A.ASMSM.\n" +
                ".M.S.M....\n" +
                "..........\n" +
                "S.S.S.S.S.\n" +
                ".A.A.A.A..\n" +
                "M.M.M.M.M.\n" +
                "..........\n";

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(grid);
        }

        // Instantiate Day4 with the test file
        Day4 day4 = new Day4(tempFile.getAbsolutePath());

        // Run Part 2 and capture the output
        int occurrences = day4.part2();

        // Assert the expected result
        assertEquals(9, occurrences, "The X-MAS pattern should appear 9 times in the given grid.");
    }

}