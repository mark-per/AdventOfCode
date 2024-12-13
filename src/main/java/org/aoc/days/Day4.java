package org.aoc.days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day4 {
    private final String filePath;

    public Day4(String filePath) {
        this.filePath = filePath;
    }

    public int runPart(int part) throws IOException {
        if (part == 1) {
            return part1();
        } else if (part == 2) {
            return part2();
        } else {
            System.out.println("Invalid part number for Day 3.");
            return -1;
        }
    }

    public int part1() throws IOException {
        int count = 0;
        String word = "XMAS";
        char[][] grid;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            grid = reader.lines().map(String::toCharArray).toArray(char[][]::new);
        }

        int rows = grid.length;
        int cols = grid[0].length;

        int[][] directions = {
                {0, 1},  // Right
                {1, 0},  // Down
                {1, 1},  // Diagonal down-right
                {-1, 1}, // Diagonal up-right
                {0, -1}, // Left
                {-1, 0}, // Up
                {-1, -1}, // Diagonal up-left
                {1, -1}  // Diagonal down-left
        };

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                for (int[] direction : directions) {
                    int dr = direction[0], dc = direction[1];
                    int nr = r, nc = c, i = 0;

                    while (i < word.length() &&
                            nr >= 0 && nr < rows &&
                            nc >= 0 && nc < cols &&
                            grid[nr][nc] == word.charAt(i)) {
                        nr += dr;
                        nc += dc;
                        i++;
                    }

                    if (i == word.length()) {
                        count++;
                    }
                }
            }
        }
        System.out.println("Part 1: " + count);
        return count; // Return the result instead of printing
    }

    public int part2() throws IOException {
        char[][] grid;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read the grid into a 2D array
            grid = reader.lines().map(String::toCharArray).toArray(char[][]::new);
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;

        // Iterate through the grid to find X-MAS patterns
        for (int r = 1; r < rows - 1; r++) {
            for (int c = 1; c < cols - 1; c++) {
                // Check for the X-MAS pattern:
                // M-S
                //  A
                // M-S
                if (grid[r][c] == 'A') {
                    if (isXMAS(grid, r, c)) {
                        count++;
                    }
                }
            }
        }

        System.out.println("Part 2: " + count);

        return count;
    }

    private boolean isXMAS(char[][] grid, int r, int c) {
        // Check all valid configurations of the X-MAS pattern
        return (
                // Standard X-MAS pattern
                (grid[r - 1][c - 1] == 'M' && grid[r - 1][c + 1] == 'S' &&
                        grid[r + 1][c - 1] == 'M' && grid[r + 1][c + 1] == 'S') ||
                        // Reversed X-MAS pattern
                        (grid[r - 1][c - 1] == 'S' && grid[r - 1][c + 1] == 'M' &&
                                grid[r + 1][c - 1] == 'S' && grid[r + 1][c + 1] == 'M') ||
                        // Left diagonal reversed, right diagonal standard
                        (grid[r - 1][c - 1] == 'S' && grid[r - 1][c + 1] == 'S' &&
                                grid[r + 1][c - 1] == 'M' && grid[r + 1][c + 1] == 'M') ||
                        // Left diagonal standard, right diagonal reversed
                        (grid[r - 1][c - 1] == 'M' && grid[r - 1][c + 1] == 'M' &&
                                grid[r + 1][c - 1] == 'S' && grid[r + 1][c + 1] == 'S')
        );
    }

}
