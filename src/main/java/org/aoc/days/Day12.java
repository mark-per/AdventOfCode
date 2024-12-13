package org.aoc.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day12 {
    private final String filePath;
    private char[][] plots;
    private int width, height;

    public Day12(String filePath) {
        this.filePath = filePath;
    }

    public long solvePart1() throws IOException {
        readGarden();
        boolean[][] visited = new boolean[height][width];

        long totalPrice = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!visited[y][x]) {
                    RegionPart1 region = new RegionPart1(0, 0);
                    traceRegionPart1(region, plots[y][x], x, y, visited);
                    totalPrice += region.price();
                }
            }
        }

        return totalPrice;
    }

    public long solvePart2() throws IOException {
        readGarden();
        boolean[][] visited = new boolean[height][width];

        long totalPrice = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!visited[y][x]) {
                    RegionPart2 region = new RegionPart2(plots[y][x], 0, 0);
                    traceRegionPart2(region, plots[y][x], x, y, visited);
                    totalPrice += region.price();
                }
            }
        }

        return totalPrice;
    }

    private void readGarden() throws IOException {
        var lines = Files.readAllLines(Paths.get(filePath));
        height = lines.size();
        width = lines.get(0).length();
        plots = new char[height][width];
        for (int r = 0; r < height; r++) {
            plots[r] = lines.get(r).toCharArray();
        }
    }

    // Part 1 region: area and perimeter
    private static class RegionPart1 {
        int area;
        int perimeter;
        RegionPart1(int area, int perimeter) {
            this.area = area;
            this.perimeter = perimeter;
        }
        long price() {
            return (long) area * perimeter;
        }
    }

    // Part 2 region: area and corners
    private static class RegionPart2 {
        char plant;
        int area;
        int corners;

        RegionPart2(char plant, int area, int corners) {
            this.plant = plant;
            this.area = area;
            this.corners = corners;
        }

        long price() {
            return (long) area * corners;
        }
    }

    // Directions for BFS
    private final int[][] DIRECTIONS = {{1,0},{-1,0},{0,1},{0,-1}};

    // Part 1 logic (area * perimeter)
    private void traceRegionPart1(RegionPart1 region, char plant, int x, int y, boolean[][] visited) {
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{x,y});
        visited[y][x] = true;

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int cx = cell[0], cy = cell[1];
            region.area += 1;

            for (int[] d : DIRECTIONS) {
                int nx = cx+d[0], ny = cy+d[1];
                if (!inBounds(nx, ny)) {
                    // Edge of map -> perimeter
                    region.perimeter++;
                } else {
                    if (plots[ny][nx] == plant) {
                        if (!visited[ny][nx]) {
                            visited[ny][nx] = true;
                            stack.push(new int[]{nx,ny});
                        }
                    } else {
                        // Different plant -> perimeter
                        region.perimeter++;
                    }
                }
            }
        }
    }

    // Part 2 logic (area * corners), using the provided "is_corner" method
    private void traceRegionPart2(RegionPart2 region, char plant, int startX, int startY, boolean[][] visited) {
        // BFS/DFS similar to part1 but with corner counting
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{startX,startY});
        visited[startY][startX] = true;

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int x = cell[0], y = cell[1];
            region.area += 1;

            // Check corners (top-left, bottom-left, top-right, bottom-right)
            if (isCorner(x,y,-1,-1)) region.corners++;
            if (isCorner(x,y,-1,1)) region.corners++;
            if (isCorner(x,y,1,-1)) region.corners++;
            if (isCorner(x,y,1,1)) region.corners++;

            // Follow region neighbors
            // left
            if (x>0 && !visited[y][x-1] && plots[y][x-1]==plant) {
                visited[y][x-1]=true;
                stack.push(new int[]{x-1,y});
            }
            // right
            if (x<width-1 && !visited[y][x+1] && plots[y][x+1]==plant) {
                visited[y][x+1]=true;
                stack.push(new int[]{x+1,y});
            }
            // up
            if (y>0 && !visited[y-1][x] && plots[y-1][x]==plant) {
                visited[y-1][x]=true;
                stack.push(new int[]{x,y-1});
            }
            // down
            if (y<height-1 && !visited[y+1][x] && plots[y+1][x]==plant) {
                visited[y+1][x]=true;
                stack.push(new int[]{x,y+1});
            }
        }
    }

    private boolean inBounds(int x, int y) {
        return x>=0 && x<width && y>=0 && y<height;
    }

    private boolean isCorner(int x, int y, int dx, int dy) {
        int total = 0;
        int direct = 0;

        if (isSame(x,y,x+dx,y)) {
            total++;
            direct++;
        }

        if (isSame(x,y,x,y+dy)) {
            total++;
            direct++;
        }

        if (isSame(x,y,x+dx,y+dy)) {
            total++;
        }

        switch (total) {
            case 0:
                return true;
            case 2:
                return direct == 2;
            case 3:
                return false;
            default:
                return direct == 0;
        }
    }

    private boolean isSame(int x, int y, int ox, int oy) {
        if (!inBounds(ox,oy)) return false;
        return plots[y][x] == plots[oy][ox];
    }

    // For manual testing
    public static void main(String[] args) throws IOException {
        Day12 solver = new Day12("src/main/resources/day12");
        System.out.println("Part 1 Result: " + solver.solvePart1());
        System.out.println("Part 2 Result: " + solver.solvePart2());
    }
}
