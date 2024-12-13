package org.aoc.days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day8 {
    private final String filePath;

    public Day8(String filePath) {
        this.filePath = filePath;
    }

    public long runPart(int part) throws IOException {
        if (part == 1) {
            return part1();
        } else if (part == 2) {
            return part2();
        } else {
            System.out.println("Invalid part number for Day 8.");
            return -1;
        }
    }

    public long part1() throws IOException {
        char[][] grid = parseInput();
        Set<Point> antinodes = calculateAntinodesPart1(grid);

        System.out.println("Total Unique Antinode Locations: " + antinodes.size());
        return antinodes.size();
    }

    public long part2() throws IOException {
        char[][] grid = parseInput();
        Set<Point> antinodes = calculateAntinodesPart2(grid);

        System.out.println("Total Unique Antinode Locations (Part 2): " + antinodes.size());
        return antinodes.size();
    }

    private char[][] parseInput() throws IOException {
        List<char[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(line.toCharArray());
            }
        }

        return rows.toArray(new char[0][]);
    }

    private Set<Point> calculateAntinodesPart1(char[][] grid) {
        Map<Character, List<Point>> antennaPositions = new HashMap<>();
        Set<Point> antinodeSet = new HashSet<>();

        int width = grid[0].length;
        int height = grid.length;

        // Gather positions of antennas grouped by frequency
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char frequency = grid[i][j];
                if (frequency != '.' && !Character.isWhitespace(frequency)) {
                    antennaPositions.putIfAbsent(frequency, new ArrayList<>());
                    antennaPositions.get(frequency).add(new Point(j, i));
                }
            }
        }

        // Calculate antinodes for each frequency
        for (Map.Entry<Character, List<Point>> entry : antennaPositions.entrySet()) {
            List<Point> positions = entry.getValue();

            for (int i = 0; i < positions.size(); i++) {
                for (int j = i + 1; j < positions.size(); j++) {
                    Point p1 = positions.get(i);
                    Point p2 = positions.get(j);

                    Point delta = p2.subtract(p1);
                    Point q1 = p2.add(delta);
                    Point q2 = p1.subtract(delta);

                    if (q1.isWithinBounds(width, height)) {
                        antinodeSet.add(q1);
                    }
                    if (q2.isWithinBounds(width, height)) {
                        antinodeSet.add(q2);
                    }

                    // Handle antinodes between p1 and p2 if distance is a multiple of 3
                    if (delta.x % 3 == 0 && delta.y % 3 == 0) {
                        Point q3 = p1.add(delta.divide(3));
                        Point q4 = p1.add(delta.multiply(2).divide(3));

                        if (q3.isWithinBounds(width, height)) {
                            antinodeSet.add(q3);
                        }
                        if (q4.isWithinBounds(width, height)) {
                            antinodeSet.add(q4);
                        }
                    }
                }
            }
        }

        return antinodeSet;
    }

    private Set<Point> calculateAntinodesPart2(char[][] grid) {
        Map<Character, List<Point>> antennaPositions = new HashMap<>();
        Set<Point> antinodeSet = new HashSet<>();

        int width = grid[0].length;
        int height = grid.length;

        // Gather positions of antennas grouped by frequency
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char frequency = grid[i][j];
                if (frequency != '.' && !Character.isWhitespace(frequency)) {
                    antennaPositions.putIfAbsent(frequency, new ArrayList<>());
                    antennaPositions.get(frequency).add(new Point(j, i));
                }
            }
        }

        // Calculate antinodes for each frequency
        for (Map.Entry<Character, List<Point>> entry : antennaPositions.entrySet()) {
            List<Point> positions = entry.getValue();

            for (int i = 0; i < positions.size(); i++) {
                for (int j = i + 1; j < positions.size(); j++) {
                    Point p1 = positions.get(i);
                    Point p2 = positions.get(j);

                    Point delta = p2.subtract(p1).normalize();

                    Point cur = p1;
                    while (cur.isWithinBounds(width, height)) {
                        antinodeSet.add(cur);
                        cur = cur.add(delta);
                    }

                    cur = p1.subtract(delta);
                    while (cur.isWithinBounds(width, height)) {
                        antinodeSet.add(cur);
                        cur = cur.subtract(delta);
                    }
                }

                // Add the antenna itself as an antinode if multiple antennas exist
                if (positions.size() > 1) {
                    antinodeSet.add(positions.get(i));
                }
            }
        }

        return antinodeSet;
    }

    private static class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Point subtract(Point other) {
            return new Point(this.x - other.x, this.y - other.y);
        }

        public Point add(Point other) {
            return new Point(this.x + other.x, this.y + other.y);
        }

        public Point multiply(int factor) {
            return new Point(this.x * factor, this.y * factor);
        }

        public Point divide(int divisor) {
            return new Point(this.x / divisor, this.y / divisor);
        }

        public Point normalize() {
            int gcd = gcd(Math.abs(x), Math.abs(y));
            return new Point(this.x / gcd, this.y / gcd);
        }

        private int gcd(int a, int b) {
            while (b != 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }

        public boolean isWithinBounds(int width, int height) {
            return x >= 0 && x < width && y >= 0 && y < height;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Point)) return false;
            Point other = (Point) obj;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}