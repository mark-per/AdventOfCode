package org.aoc.days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day2 {
    private final String filePath;

    public Day2(String filePath) {
        this.filePath = filePath;
    }

    public int runPart(int part) throws IOException {
        switch (part) {
            case 1:
                part1();
                break;
            case 2:
                part2();
                break;
            default:
                System.out.println("Invalid part number.");

        }
        return -1;
    }

    public void part1() throws IOException {
        int safeReports = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] levels = line.trim().split("\\s+");
                if (isSafeReport(levels)) {
                    safeReports++;
                }
            }
        }

        System.out.println("Safe Reports (Part 1): " + safeReports + "\n");
    }

    public void part2() throws IOException {
        int safeReports = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] levels = line.trim().split("\\s+");
                if (isSafeReport(levels) || canBeMadeSafe(levels)) {
                    safeReports++;
                }
            }
        }

        System.out.println("Safe Reports (Part 2): " + safeReports + "\n");
    }

    private boolean isSafeReport(String[] levels) {
        int[] nums = new int[levels.length];
        for (int i = 0; i < levels.length; i++) {
            nums[i] = Integer.parseInt(levels[i]);
        }

        boolean isIncreasing = true;
        boolean isDecreasing = true;

        for (int i = 1; i < nums.length; i++) {
            int diff = nums[i] - nums[i - 1];

            // Check difference is between 1 and 3
            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                return false;
            }

            if (diff > 0) isDecreasing = false;
            if (diff < 0) isIncreasing = false;
        }

        return isIncreasing || isDecreasing;
    }

    private boolean canBeMadeSafe(String[] levels) {
        int[] nums = new int[levels.length];
        for (int i = 0; i < levels.length; i++) {
            nums[i] = Integer.parseInt(levels[i]);
        }

        for (int i = 0; i < nums.length; i++) {
            // Create a new array with one element removed
            int[] modified = new int[nums.length - 1];
            for (int j = 0, k = 0; j < nums.length; j++) {
                if (j != i) {
                    modified[k++] = nums[j];
                }
            }

            // Check if the modified report is safe
            if (isSafe(modified)) {
                return true;
            }
        }

        return false;
    }

    private boolean isSafe(int[] nums) {
        boolean isIncreasing = true;
        boolean isDecreasing = true;

        for (int i = 1; i < nums.length; i++) {
            int diff = nums[i] - nums[i - 1];

            // Check difference is between 1 and 3
            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                return false;
            }

            if (diff > 0) isDecreasing = false;
            if (diff < 0) isIncreasing = false;
        }

        return isIncreasing || isDecreasing;
    }
}
