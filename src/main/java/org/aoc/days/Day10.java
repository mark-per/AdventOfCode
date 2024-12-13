package org.aoc.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day10 {
    private final String filePath;
    private static final int SIZE = 64;

    public Day10(String filePath) {
        this.filePath = filePath;
    }

    public long runPart(int part) throws IOException {
        String input = Files.readString(Paths.get(filePath)).trim();
        if (part == 1) {
            return solvePart1(input);
        } else if (part == 2) {
            return solvePart2(input);
        } else {
            throw new IllegalArgumentException("Invalid part: " + part);
        }
    }

    /**
     * PART 1 LOGIC (Adapted from the first Rust code snippet):
     * - Parse the map
     * - Identify bottoms and tops
     * - Initialize scores
     * - For each top, call computeScores (with visited)
     * - Sum scores at bottoms
     */
    private long solvePart1(String input) {
        Integer[][] map = new Integer[SIZE][SIZE];
        List<int[]> bottoms = new ArrayList<>();
        List<int[]> tops = new ArrayList<>();
        int[][] scores = new int[SIZE][SIZE];

        parseInput(input, map, bottoms, tops);

        // For each top, compute scores
        for (int[] top : tops) {
            int tx = top[0], ty = top[1];
            boolean[][] visited = new boolean[SIZE][SIZE];
            computeScores(tx, ty, map, scores, visited);
        }

        // sum scores at bottoms
        long result = 0;
        for (int[] b : bottoms) {
            int bx = b[0], by = b[1];
            result += scores[by][bx];
        }

        return result;
    }

    /**
     * PART 2 LOGIC (Adapted from the second Rust code snippet):
     * - Similar parsing
     * - Identify bottoms and tops
     * - Initialize ratings
     * - For each top, call computeRatings (no visited)
     * - Sum ratings at bottoms
     */
    private long solvePart2(String input) {
        Integer[][] map = new Integer[SIZE][SIZE];
        List<int[]> bottoms = new ArrayList<>();
        List<int[]> tops = new ArrayList<>();
        int[][] ratings = new int[SIZE][SIZE];

        parseInput(input, map, bottoms, tops);

        // Initialize ratings for tops
        for (int[] top : tops) {
            int tx = top[0], ty = top[1];
            ratings[ty][tx] = 1;
        }

        // For each top, compute ratings
        for (int[] top : tops) {
            int tx = top[0], ty = top[1];
            computeRatings(tx, ty, map, ratings);
        }

        // sum ratings at bottoms
        long result = 0;
        for (int[] b : bottoms) {
            int bx = b[0], by = b[1];
            result += ratings[by][bx];
        }

        return result;
    }

    private void parseInput(String input, Integer[][] map, List<int[]> bottoms, List<int[]> tops) {
        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length && y < SIZE; y++) {
            char[] line = lines[y].toCharArray();
            for (int x = 0; x < line.length && x < SIZE; x++) {
                char c = line[x];
                if (c == '.') {
                    map[y][x] = null;
                } else if (c == '0') {
                    map[y][x] = 0;
                    bottoms.add(new int[]{x, y});
                } else if (c == '9') {
                    map[y][x] = 9;
                    tops.add(new int[]{x, y});
                } else {
                    // other digits
                    int val = c - '0';
                    map[y][x] = val;
                }
            }
        }
    }

    // Directions for up, down, left, right
    private static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

    /**
     * computeScores for Part 1:
     * From a top cell (x,y), move to neighbors of height (cur_height-1), increment their score, mark visited, recurse.
     */
    private void computeScores(int x, int y, Integer[][] map, int[][] scores, boolean[][] visited) {
        Integer curH = map[y][x];
        if (curH == null) return;

        for (int[] d : DIRS) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (!inBounds(nx, ny)) continue;
            if (visited[ny][nx]) continue;

            Integer nh = map[ny][nx];
            if (nh != null && curH > 0 && nh == curH - 1) {
                scores[ny][nx] += 1;
                visited[ny][nx] = true;
                computeScores(nx, ny, map, scores, visited);
            }
        }
    }

    /**
     * computeRatings for Part 2:
     * Similar logic but without visited. Might lead to repeated increments if not careful,
     * but we follow the provided logic. For every neighbor with height = cur_height-1,
     * increment ratings and recurse.
     */
    private void computeRatings(int x, int y, Integer[][] map, int[][] ratings) {
        Integer curH = map[y][x];
        if (curH == null) return;

        for (int[] d : DIRS) {
            int nx = x + d[0];
            int ny = y + d[1];
            if (!inBounds(nx, ny)) continue;

            Integer nh = map[ny][nx];
            if (nh != null && curH > 0 && nh == curH - 1) {
                ratings[ny][nx] += 1;
                computeRatings(nx, ny, map, ratings);
            }
        }
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    // For manual testing
    public static void main(String[] args) throws IOException {
        Day10 solver = new Day10("src/main/resources/day10"); // adjust path as needed
        System.out.println("Part 1 Result: " + solver.runPart(1));
        System.out.println("Part 2 Result: " + solver.runPart(2));
    }
}
