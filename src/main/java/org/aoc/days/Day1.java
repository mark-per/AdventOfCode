package org.aoc.days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day1 {
    private final List<Integer> left = new ArrayList<>();
    private final List<Integer> right = new ArrayList<>();

    public int runPart(int part) {
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

    public Day1(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+"); // Split by whitespace
                if (parts.length == 2) {
                    left.add(Integer.parseInt(parts[0]));
                    right.add(Integer.parseInt(parts[1]));
                }
            }
        }
        // Sort the lists
        left.sort((o1, o2) -> o2 - o1);
        right.sort((o1, o2) -> o2 - o1);
    }



    public int part1() {
        int totalDifference = 0;

        for (int i = 0; i < left.size(); i++) {
            totalDifference += Math.abs(left.get(i) - right.get(i));
        }

        System.out.println("Total Difference: " + totalDifference + "\n");
        return totalDifference;
    }

    public int part2() {
        Map<Integer, Integer> leftmap = new HashMap<>();
        Map<Integer, Integer> rightmap = new HashMap<>();

        for(int i = 0; i < left.size(); i++) {
            leftmap.put(left.get(i), leftmap.getOrDefault(left.get(i), 0) + 1);
            rightmap.put(right.get(i), rightmap.getOrDefault(right.get(i), 0) + 1);
        }

        var res = 0;

        for(Map.Entry<Integer, Integer> entry : leftmap.entrySet()) {
            res += entry.getKey() * entry.getValue() * rightmap.getOrDefault(entry.getKey(), 0);
        }

        System.out.println("Similarity Score  : " + res + "\n");
        return res;
    }
}
