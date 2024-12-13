package org.aoc.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day13 {
    private final String filePath;
    private List<Equation> equations;

    public Day13(String filePath) {
        this.filePath = filePath;
    }

    public long solvePart2() throws IOException {
        parseInput();
        long OFFSET = 10000000000000L;
        long totalCost = 0;
        for (Equation eq : equations) {
            // For part 1, offset x and y by a large amount before solving
            Equation adjusted = new Equation(eq.dx_a, eq.dy_a, eq.dx_b, eq.dy_b, eq.x + OFFSET, eq.y + OFFSET);
            Long[] solution = adjusted.solve();
            if (solution != null) {
                long A = solution[0];
                long B = solution[1];
                long cost = A * 3 + B; // A-button = 3 tokens, B-button = 1 token
                totalCost += cost;
            }
        }
        return totalCost;
    }

    public long solvePart1() throws IOException {
        parseInput();
        long totalCost = 0;
        for (Equation eq : equations) {
            // For part 2, use x and y as-is
            Long[] solution = eq.solve();
            if (solution != null) {
                long A = solution[0];
                long B = solution[1];
                long cost = A * 3 + B;
                totalCost += cost;
            }
        }
        return totalCost;
    }

    private void parseInput() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        equations = new ArrayList<>();
        int i = 0;
        while (i < lines.size()) {
            // Skip empty lines
            while (i < lines.size() && lines.get(i).trim().isEmpty()) {
                i++;
            }
            if (i + 2 >= lines.size()) break; // not enough lines for a full machine description

            String lineA = lines.get(i++).trim();
            String lineB = lines.get(i++).trim();
            String lineP = lines.get(i++).trim();

            // Parse line A: "Button A: X+<dx_a>, Y+<dy_a>"
            long dx_a, dy_a;
            {
                String part = lineA.substring("Button A: X+".length());
                int commaIndex = part.indexOf(',');
                String dxAstr = part.substring(0, commaIndex).trim();
                dx_a = Long.parseLong(dxAstr);
                String rest = part.substring(commaIndex + 1).trim(); // "Y+<dy_a>"
                rest = rest.substring(2); // remove "Y+"
                dy_a = Long.parseLong(rest.trim());
            }

            // Parse line B: "Button B: X+<dx_b>, Y+<dy_b>"
            long dx_b, dy_b;
            {
                String part = lineB.substring("Button B: X+".length());
                int commaIndex = part.indexOf(',');
                String dxBstr = part.substring(0, commaIndex).trim();
                dx_b = Long.parseLong(dxBstr);
                String rest = part.substring(commaIndex + 1).trim(); // "Y+<dy_b>"
                rest = rest.substring(2); // remove "Y+"
                dy_b = Long.parseLong(rest.trim());
            }

            // Parse line P: "Prize: X=<x>, Y=<y>"
            long x, y;
            {
                String part = lineP.substring("Prize: X=".length());
                int commaIndex = part.indexOf(',');
                String xStr = part.substring(0, commaIndex).trim();
                x = Long.parseLong(xStr);
                String rest = part.substring(commaIndex + 1).trim(); // "Y=<y>"
                rest = rest.substring(2); // remove "Y="
                y = Long.parseLong(rest.trim());
            }

            equations.add(new Equation(dx_a, dy_a, dx_b, dy_b, x, y));
        }
    }

    private static class Equation {
        long dx_a, dy_a, dx_b, dy_b, x, y;

        Equation(long dx_a, long dy_a, long dx_b, long dy_b, long x, long y) {
            this.dx_a = dx_a; this.dy_a = dy_a;
            this.dx_b = dx_b; this.dy_b = dy_b;
            this.x = x; this.y = y;
        }

        // Solve the linear system as per the Rust code logic:
        // Returns (A,B) or null if no solution
        Long[] solve() {
            long n0 = dy_b * x;
            long n1 = dx_b * y;
            long m0 = dx_a * y;
            long m1 = dy_a * x;

            long det0 = dx_a * dy_b;
            long det1 = dx_b * dy_a;
            if (det1 == det0) {
                return null; // no solution if determinant is zero
            }

            // Check conditions from the provided logic
            if (det0 > det1 && n0 > n1 && m0 > m1) {
                long det = det0 - det1;
                long n = n0 - n1;
                long m = m0 - m1;
                if (n % det == 0 && m % det == 0) {
                    long A = n/det;
                    long B = m/det;
                    if (A >= 0 && B >= 0) {
                        return new Long[]{A,B};
                    }
                }
            } else if (det1 > det0 && n1 > n0 && m1 > m0) {
                long det = det1 - det0;
                long n = n1 - n0;
                long m = m1 - m0;
                if (n % det == 0 && m % det == 0) {
                    long A = n/det;
                    long B = m/det;
                    if (A >= 0 && B >= 0) {
                        return new Long[]{A,B};
                    }
                }
            }

            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        Day13 solver = new Day13("src/main/resources/day13");
        System.out.println("Part 1 Result: " + solver.solvePart1());
        System.out.println("Part 2 Result: " + solver.solvePart2());
    }
}
