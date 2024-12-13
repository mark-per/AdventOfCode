package org.aoc.days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Day6 {

//    todo: optimize part two

    private final String filePath;

    public Day6(String filePath) {
        this.filePath = filePath;
    }

    public int runPart(int part) throws IOException {
        if (part == 1) {
            return part1();
        } else if (part == 2) {
            return part2();
        } else {
            System.out.println("Invalid part number for Day 6.");
            return -1;
        }
    }

    public int part1() throws IOException {
        char[][] map = parseMap();
        return simulateGuardPart1(map);
    }

    public int part2() throws IOException {
        char[][] map = parseMap();
        int rows = map.length;
        int cols = map[0].length;
        int validObstructionPositions = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c] == '.' && !isGuardStartingPosition(map, r, c)) {
                    map[r][c] = '#'; // Temporarily place an obstruction
                    if (simulateGuard(map)) {
                        validObstructionPositions++;
                    }
                    map[r][c] = '.'; // Remove the obstruction
                }
            }
        }

        System.out.println("Valid obstruction positions: " + validObstructionPositions +"\n");

        return validObstructionPositions;
    }

    private int simulateGuardPart1(char[][] map) {
        int rows = map.length;
        int cols = map[0].length;

        int guardX = -1, guardY = -1;
        char direction = ' ';
        outer:
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ("^>v<".indexOf(map[r][c]) != -1) {
                    guardX = r;
                    guardY = c;
                    direction = map[r][c];
                    map[r][c] = '.'; // Clear the guard's starting position
                    break outer;
                }
            }
        }

        if (guardX == -1 || guardY == -1) {
            throw new IllegalStateException("Guard's starting position not found.");
        }

        Set<String> visited = new HashSet<>();
        visited.add(guardX + "," + guardY);

        int[][] deltas = {
                {-1, 0}, // Up
                {0, 1},  // Right
                {1, 0},  // Down
                {0, -1}  // Left
        };
        String directions = "^>v<";

        while (true) {
            int dirIndex = directions.indexOf(direction);
            int nextX = guardX + deltas[dirIndex][0];
            int nextY = guardY + deltas[dirIndex][1];

            if (nextX < 0 || nextY < 0 || nextX >= rows || nextY >= cols) {
                break; // Guard leaves the map
            }

            if (map[nextX][nextY] == '#') {
                direction = directions.charAt((dirIndex + 1) % 4); // Turn right
            } else {
                guardX = nextX;
                guardY = nextY;
                visited.add(guardX + "," + guardY);
            }
        }

        System.out.println("Visited obstruction positions: " + visited.size() + "\n");
        return visited.size();
    }

    private boolean simulateGuard(char[][] map) {
        int rows = map.length;
        int cols = map[0].length;

        int guardX = -1, guardY = -1;
        char direction = ' ';
        outer:
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ("^>v<".indexOf(map[r][c]) != -1) {
                    guardX = r;
                    guardY = c;
                    direction = map[r][c];
                    break outer;
                }
            }
        }

        if (guardX == -1 || guardY == -1) {
            throw new IllegalStateException("Guard's starting position not found.");
        }

        Set<String> visitedStates = new HashSet<>();
        visitedStates.add(guardX + "," + guardY + "," + direction);

        int[][] deltas = {
                {-1, 0}, {0, 1}, {1, 0}, {0, -1}
        };
        String directions = "^>v<";

        int iterations = 0;
        while (iterations++ < 10_000) { // Prevent infinite loops
            int dirIndex = directions.indexOf(direction);
            int nextX = guardX + deltas[dirIndex][0];
            int nextY = guardY + deltas[dirIndex][1];

            if (nextX < 0 || nextY < 0 || nextX >= rows || nextY >= cols) {
                return false;
            }

            if (map[nextX][nextY] == '#') {
                direction = directions.charAt((dirIndex + 1) % 4);
            } else {
                guardX = nextX;
                guardY = nextY;
            }

            String state = guardX + "," + guardY + "," + direction;
            if (visitedStates.contains(state)) {
                return true;
            }
            visitedStates.add(state);
        }

        return false;
    }

    private boolean isGuardStartingPosition(char[][] map, int r, int c) {
        return "^>v<".indexOf(map[r][c]) != -1;
    }

    private char[][] parseMap() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines()
                    .map(String::toCharArray)
                    .toArray(char[][]::new);
        }
    }
}
