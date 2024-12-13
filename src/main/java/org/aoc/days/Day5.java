package org.aoc.days;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day5 {
    private final String filePath;

    public Day5(String filePath) {
        this.filePath = filePath;
    }

    public int runPart(int part) throws IOException {
        if (part == 1) {
            return part1();
        } else if (part == 2) {
            return part2();
        } else {
            System.out.println("Invalid part number for Day 5.");
            return -1;
        }
    }

    public int part1() throws IOException {
        // Parse input data
        InputData inputData = parseInputData(filePath);

        int sumOfMiddlePages = 0;

        // Check each update
        for (List<Integer> update : inputData.updates) {
            if (isCorrectOrder(update, inputData.precedenceMap)) {
                int middlePage = findMiddlePage(update);
                sumOfMiddlePages += middlePage;
            }
        }

        System.out.println("Sum of middle pages: " + sumOfMiddlePages);
        return sumOfMiddlePages;
    }

    public int part2() throws IOException {
        // Parse input data
        InputData inputData = parseInputData(filePath);

        int sumOfMiddlePages = 0;

        for (List<Integer> update : inputData.updates) {
            if (!isCorrectOrder(update, inputData.precedenceMap)) {
                // Reorder the update using topological sort
                List<Integer> reorderedUpdate = reorderPages(update, inputData.precedenceMap);

                // Find and sum the middle page
                int middlePage = findMiddlePage(reorderedUpdate);
                sumOfMiddlePages += middlePage;
            }
        }

        System.out.println("Sum of middle pages after reordering: " + sumOfMiddlePages);
        return sumOfMiddlePages;
    }

    private InputData parseInputData(String filePath) throws IOException {
        List<String> orderingRules = new ArrayList<>();
        List<List<Integer>> updates = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            boolean readingRules = true;

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    readingRules = false;
                    continue;
                }

                if (readingRules) {
                    orderingRules.add(line.trim());
                } else {
                    String[] pages = line.trim().split(",");
                    List<Integer> update = new ArrayList<>();
                    for (String page : pages) {
                        update.add(Integer.parseInt(page));
                    }
                    updates.add(update);
                }
            }
        }

        // Parse rules into a precedence map
        Map<Integer, Set<Integer>> precedenceMap = new HashMap<>();
        for (String rule : orderingRules) {
            String[] parts = rule.split("\\|");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            precedenceMap.putIfAbsent(x, new HashSet<>());
            precedenceMap.get(x).add(y);
        }

        return new InputData(precedenceMap, updates);
    }

    private boolean isCorrectOrder(List<Integer> update, Map<Integer, Set<Integer>> precedenceMap) {
        Set<Integer> pagesInUpdate = new HashSet<>(update);

        for (int i = 0; i < update.size(); i++) {
            int currentPage = update.get(i);

            if (precedenceMap.containsKey(currentPage)) {
                for (int requiredAfter : precedenceMap.get(currentPage)) {
                    if (pagesInUpdate.contains(requiredAfter) && update.indexOf(requiredAfter) < i) {
                        return false; // Rule violated
                    }
                }
            }
        }

        return true;
    }

    private List<Integer> reorderPages(List<Integer> update, Map<Integer, Set<Integer>> precedenceMap) {
        Set<Integer> pagesInUpdate = new HashSet<>(update);

        // Filter rules to only include relevant pages
        Map<Integer, Set<Integer>> filteredMap = new HashMap<>();
        for (int page : pagesInUpdate) {
            if (precedenceMap.containsKey(page)) {
                Set<Integer> filteredSet = new HashSet<>();
                for (int dependentPage : precedenceMap.get(page)) {
                    if (pagesInUpdate.contains(dependentPage)) {
                        filteredSet.add(dependentPage);
                    }
                }
                if (!filteredSet.isEmpty()) {
                    filteredMap.put(page, filteredSet);
                }
            }
        }

        // Perform topological sort
        List<Integer> sortedPages = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Set<Integer> visiting = new HashSet<>();

        for (int page : update) {
            if (!visited.contains(page)) {
                if (!topologicalSort(page, filteredMap, visited, visiting, sortedPages)) {
                    throw new RuntimeException("Cycle detected in precedence rules!");
                }
            }
        }

        return sortedPages;
    }

    private boolean topologicalSort(int page, Map<Integer, Set<Integer>> precedenceMap,
                                    Set<Integer> visited, Set<Integer> visiting, List<Integer> sortedPages) {
        if (visiting.contains(page)) {
            return false; // Cycle detected
        }

        if (!visited.contains(page)) {
            visiting.add(page);

            if (precedenceMap.containsKey(page)) {
                for (int dependentPage : precedenceMap.get(page)) {
                    if (!topologicalSort(dependentPage, precedenceMap, visited, visiting, sortedPages)) {
                        return false;
                    }
                }
            }

            visiting.remove(page);
            visited.add(page);
            sortedPages.add(page);
        }

        return true;
    }

    private int findMiddlePage(List<Integer> update) {
        return update.get(update.size() / 2);
    }

    private static class InputData {
        Map<Integer, Set<Integer>> precedenceMap;
        List<List<Integer>> updates;

        InputData(Map<Integer, Set<Integer>> precedenceMap, List<List<Integer>> updates) {
            this.precedenceMap = precedenceMap;
            this.updates = updates;
        }
    }
}
