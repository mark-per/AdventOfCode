package org.aoc.days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day11 {
    private final String filePath;

    public Day11(String filePath) {
        this.filePath = filePath;
    }

    public long runPart(int part) throws IOException {
        String input = Files.readString(Paths.get(filePath)).trim();
        switch (part) {
            case 1:
                return solvePart1(input);
            case 2:
                return solvePart2(input);
            default:
                throw new IllegalArgumentException("Invalid part: " + part);
        }
    }

    // =============================
    // PART 1 LOGIC
    // =============================
    private long solvePart1(String input) {
        final int ITERATIONS = 25;
        List<Long> numbers = parseNumbers(input);
        Map<CacheKey, Integer> cache = new HashMap<>();

        long result = 0;
        for (long num : numbers) {
            result += simulateNum(num, 1, ITERATIONS, cache);
        }
        return result;
    }

    private static class CacheKey {
        long num;
        int iteration;
        CacheKey(long num, int iteration) {
            this.num = num;
            this.iteration = iteration;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof CacheKey)) return false;
            CacheKey other = (CacheKey)o;
            return this.num == other.num && this.iteration == other.iteration;
        }

        @Override
        public int hashCode() {
            return Objects.hash(num, iteration);
        }
    }

    private int simulateNum(long num, int iteration, int ITERATIONS, Map<CacheKey, Integer> cache) {
        CacheKey key = new CacheKey(num, iteration);
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        int result;
        if (num == 0) {
            if (iteration == ITERATIONS) {
                result = 1;
            } else {
                result = simulateNum(1, iteration+1, ITERATIONS, cache);
            }
            cache.put(key, result);
            return result;
        }

        int numDigits = countDigits(num);
        if (numDigits % 2 == 0) {
            if (iteration == ITERATIONS) {
                result = 2;
            } else {
                long mask = (long)Math.pow(10, numDigits/2);
                long firstHalf = num / mask;
                long secondHalf = num % mask;
                result = simulateNum(firstHalf, iteration+1, ITERATIONS, cache)
                        + simulateNum(secondHalf, iteration+1, ITERATIONS, cache);
            }
        } else {
            if (iteration == ITERATIONS) {
                result = 1;
            } else {
                result = simulateNum(num * 2024, iteration+1, ITERATIONS, cache);
            }
        }

        cache.put(key, result);
        return result;
    }

    // =============================
    // PART 2 LOGIC
    // =============================
    // ITERATIONS=10000
    // We build a lookup table for transformations (Next).
    // Then we simulate 10000 iterations using the lookup table to find how the stones evolve.

    private long solvePart2(String input) {
        final int ITERATIONS = 75;
        List<Long> numbers = parseNumbers(input);

        // Build lookup table
        Map<Long, Next> lookup = new HashMap<>();
        TreeSet<Long> todos = new TreeSet<>(numbers);

        while (!todos.isEmpty()) {
            long todo = todos.pollFirst();
            long current = todo;
            while (true) {
                if (lookup.containsKey(current)) {
                    // cycle found or already processed
                    break;
                }
                Next nxt = nextPart2(current);
                lookup.put(current, nxt);
                if (nxt.isSingle()) {
                    long nxtNum = nxt.singleVal();
                    current = nxtNum;
                } else {
                    long f = nxt.firstVal();
                    long s = nxt.secondVal();
                    if (!lookup.containsKey(s)) {
                        todos.add(s);
                    }
                    current = f;
                }
            }
        }

        // Count initial occurrences
        Map<Long, Long> counts = new HashMap<>();
        for (long num : numbers) {
            counts.put(num, counts.getOrDefault(num, 0L) + 1);
        }

        // Simulate ITERATIONS times
        for (int i = 0; i < ITERATIONS; i++) {
            Map<Long, Long> nextCounts = new HashMap<>();
            for (Map.Entry<Long, Long> e : counts.entrySet()) {
                long num = e.getKey();
                long count = e.getValue();
                Next nxt = lookup.get(num);

                if (nxt.isSingle()) {
                    long nVal = nxt.singleVal();
                    nextCounts.put(nVal, nextCounts.getOrDefault(nVal, 0L) + count);
                } else {
                    long f = nxt.firstVal();
                    long s = nxt.secondVal();
                    nextCounts.put(f, nextCounts.getOrDefault(f, 0L) + count);
                    nextCounts.put(s, nextCounts.getOrDefault(s, 0L) + count);
                }
            }
            counts = nextCounts;
        }

        // Sum final counts
        long result = 0;
        for (long c : counts.values()) {
            result += c;
        }

        return result;
    }

    private static class Next {
        Long single;
        Long first;
        Long second;
        static Next single(long num) {
            Next n = new Next();
            n.single = num;
            return n;
        }
        static Next dbl(long f, long s) {
            Next n = new Next();
            n.first = f;
            n.second = s;
            return n;
        }

        boolean isSingle() {
            return single != null;
        }
        long singleVal() {
            return single;
        }
        long firstVal() {
            return first;
        }
        long secondVal() {
            return second;
        }
    }

    private Next nextPart2(long num) {
        if (num == 0) {
            return Next.single(1);
        }
        int digits = countDigits(num);
        if (digits % 2 == 0) {
            long mask = (long)Math.pow(10, digits/2);
            long firstHalf = num / mask;
            long secondHalf = num % mask;
            return Next.dbl(firstHalf, secondHalf);
        }
        return Next.single(num * 2024);
    }

    private List<Long> parseNumbers(String input) {
        String[] parts = input.split("\\s+");
        List<Long> nums = new ArrayList<>();
        for (String p : parts) {
            if (!p.isEmpty()) {
                nums.add(Long.parseLong(p));
            }
        }
        return nums;
    }

    private static int countDigits(long num) {
        if (num == 0) return 1;
        return (int)Math.floor(Math.log10(num)) + 1;
    }
}
