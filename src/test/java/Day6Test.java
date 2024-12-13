package org.aoc.days;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day6Test {

    @Test
    public void testGuardPath() throws IOException {
        // Create a temporary test file with the given grid
        File tempFile = File.createTempFile("day6_test", ".txt");
        tempFile.deleteOnExit();

        String grid =
                "....#.....\n" +
                        ".........#\n" +
                        "..........\n" +
                        "..#.......\n" +
                        ".......#..\n" +
                        "..........\n" +
                        ".#..^.....\n" +
                        "........#.\n" +
                        "#.........\n" +
                        "......#...\n";

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(grid);
        }

        // Instantiate Day6 with the test file
        Day6 day6 = new Day6(tempFile.getAbsolutePath());

        // Run Part 1 and capture the output
        int visitedPositions = day6.part1();

        // Assert the expected result
        assertEquals(41, visitedPositions, "The guard should visit 41 distinct positions in the given grid.");
    }

    @Test
    public void testGuardStuckPositions() throws IOException {
        String map = "....#.....\n" +
                ".........#\n" +
                "..........\n" +
                "..#.......\n" +
                ".......#..\n" +
                "..........\n" +
                ".#..^.....\n" +
                "........#.\n" +
                "#.........\n" +
                "......#...\n";

        File tempFile = File.createTempFile("day6_test", ".txt");
        tempFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(map);
        }

        Day6 day6 = new Day6(tempFile.getAbsolutePath());

        // Part 1 Test
        int visitedPositions = day6.runPart(1);
        assertEquals(41, visitedPositions, "Part 1 should detect 41 distinct positions visited by the guard.");

        // Part 2 Test
        int loopObstructions = day6.runPart(2);
        assertEquals(6, loopObstructions, "Part 2 should detect 6 positions for obstructions to create a loop.");
    }

}
