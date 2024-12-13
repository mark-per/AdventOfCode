package org.aoc.days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day7 {
    private final String filePath;
    private Set<String> visitedStates;

    public Day7(String filePath) {
        this.filePath = filePath;
    }

    public long runPart(int part) throws IOException {
        if (part == 1) {
            return part1();
        } else if (part == 2) {
            return part2();
        } else {
            System.out.println("Invalid part number for Day 7.");
            return -1;
        }
    }

    public long part1() throws IOException {
        List<Equation> equations = parseInput();
        long totalCalibrationResult = 0;

        for (Equation eq : equations) {
            visitedStates = new HashSet<>(); // Reset memoization for each equation
            boolean canSolve = canEvaluateToTarget(eq.target, eq.numbers);
            System.out.println("Equation: " + eq.numbers + " Target: " + eq.target + " Can Solve: " + canSolve);

            if (canSolve) {
                totalCalibrationResult += eq.target;
            }
        }

        System.out.println("Total Calibration Result: " + totalCalibrationResult);
        return totalCalibrationResult;
    }

    public long part2() throws IOException {
        List<Equation> equations = parseInput();
        long totalCalibrationResult = 0;

        for (Equation eq : equations) {
            visitedStates = new HashSet<>(); // Reset memoization for each equation
            boolean canSolve = canEvaluateToTargetWithConcat(eq.target, eq.numbers);
            System.out.println("Equation: " + eq.numbers + " Target: " + eq.target + " Can Solve: " + canSolve);

            if (canSolve) {
                totalCalibrationResult += eq.target;
            }
        }

        System.out.println("Total Calibration Result with Concatenation: " + totalCalibrationResult);
        return totalCalibrationResult;
    }

    private boolean canEvaluateToTarget(long target, List<Long> numbers) {
        if (numbers.isEmpty()) {
            return false;
        }
        return evaluate(target, numbers, 1, numbers.get(0)); // Start from the second number
    }

    private boolean canEvaluateToTargetWithConcat(long target, List<Long> numbers) {
        if (numbers.isEmpty()) {
            return false;
        }
        return evaluateWithConcat(target, numbers, 1, numbers.get(0)); // Start from the second number
    }

    private boolean evaluate(long target, List<Long> numbers, int index, long currentResult) {
        if (index == numbers.size()) {
            return currentResult == target;
        }

        long nextNumber = numbers.get(index);

        boolean addition = evaluate(target, numbers, index + 1, currentResult + nextNumber);
        boolean multiplication = evaluate(target, numbers, index + 1, currentResult * nextNumber);

        return addition || multiplication;
    }

    private boolean evaluateWithConcat(long target, List<Long> numbers, int index, long currentResult) {
        if (index == numbers.size()) {
            return currentResult == target;
        }

        long nextNumber = numbers.get(index);

        // Recursive case: try all operations
        boolean addition = evaluateWithConcat(target, numbers, index + 1, currentResult + nextNumber);
        boolean multiplication = evaluateWithConcat(target, numbers, index + 1, currentResult * nextNumber);
        boolean concatenation = evaluateWithConcat(target, numbers, index + 1, concatNumbers(currentResult, nextNumber));

        return addition || multiplication || concatenation;
    }

    private long concatNumbers(long left, long right) {
        String concatenated = String.valueOf(left) + right;
        return Long.parseLong(concatenated);
    }

    private List<Equation> parseInput() throws IOException {
        List<Equation> equations = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] parts = line.split(": ");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid format: " + line);
                }

                long target = Long.parseLong(parts[0].trim()); // Trim the target value
                String[] numberStrings = parts[1].split(" ");
                List<Long> numbers = new ArrayList<>();
                for (String num : numberStrings) {
                    numbers.add(Long.parseLong(num.trim())); // Trim each number
                }
                equations.add(new Equation(target, numbers));
            }
        }

        return equations;
    }


    private static class Equation {
        long target;
        List<Long> numbers;

        Equation(long target, List<Long> numbers) {
            this.target = target;
            this.numbers = numbers;
        }
    }
}
