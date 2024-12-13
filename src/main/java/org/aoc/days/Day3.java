package org.aoc.days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
    private final String filePath;

    public Day3(String filePath) {
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
        int totalSum = 0;
        Pattern mulPattern = Pattern.compile("mul\\((\\d+),(\\d+)\\)");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher mulMatcher = mulPattern.matcher(line);
                while (mulMatcher.find()) {
                    int x = Integer.parseInt(mulMatcher.group(1));
                    int y = Integer.parseInt(mulMatcher.group(2));
                    totalSum += x * y;
                }
            }
        }

        System.out.println("Total Sum of Valid Multiplications (Day 3, Part 1): " + totalSum +"\n");
    }

    public void part2() throws IOException {
        int totalSum = 0;
        boolean mulEnabled = true;
        Pattern instructionPattern = Pattern.compile("(do\\(\\))|(don't\\(\\))|mul\\((\\d+),(\\d+)\\)");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher instructionMatcher = instructionPattern.matcher(line);

                while (instructionMatcher.find()) {
                    String doMatch = instructionMatcher.group(1);
                    String dontMatch = instructionMatcher.group(2);
                    String mulMatch = instructionMatcher.group(3);

                    if (doMatch != null) {
                        mulEnabled = true;
                    } else if (dontMatch != null) {
                        mulEnabled = false;
                    }
                    else if (mulMatch != null && mulEnabled) {
                        int x = Integer.parseInt(instructionMatcher.group(3));
                        int y = Integer.parseInt(instructionMatcher.group(4));
                        totalSum += x * y;
                    }
                }
            }
        }

        System.out.println("Total Sum of Enabled Multiplications (Day 3, Part 2): " + totalSum +"\n");
    }
}
