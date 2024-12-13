import org.aoc.days.Day11;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Test cases for Day11 logic.
 * These tests assume that Day11 has a method to run the simulation given:
 * - A string input representing the initial stones.
 * - A number of blinks.
 * That method should return a List<List<Long>> or a final count of stones after blinking.
 *
 * If your Day11 class does not currently have a direct method like this, you may need to refactor it:
 * - Add a public method: `public long simulate(String input, int blinks)` that returns the number of stones after `blinks`.
 *
 * For simplicity, let's assume we've added:
 * public long simulate(String input, int blinks) {
 *    // Similar logic to runPart, but directly simulates the blinking process and returns count.
 *    // Or reuse runPart(1) if part=1 means 25 blinks, etc.
 *    // This depends on how your code is structured.
 * }
 */
public class Day11Test {

    /**
     * Test the single-step transformation described in the puzzle:
     * Initial: 0 1 10 99 999
     * After 1 blink:
     * becomes: 1 2024 1 0 9 9 2021976
     *
     * We verify both the final arrangement and the count of stones.
     */
    @Test
    public void testSingleStepExample() throws IOException {
        Day11 solver = new Day11(null); // if needed, adjust constructor or make a separate method
        String initial = "0 1 10 99 999";
        // We know after 1 blink:
        // 0 -> 1
        // 1 -> 2024 (odd digits -> multiply by 2024)
        // 10 -> split into 1 and 0 (even digits)
        // 99 -> split into two 9's (even digits)
        // 999 -> odd digits -> multiply by 2024 => 999*2024=2021976
        //
        // final: 1 2024 1 0 9 9 2021976
        // Count: 7 stones
        List<Long> expected = Arrays.asList(1L, 2024L, 1L, 0L, 9L, 9L, 2021976L);

        List<Long> resultArrangement = simulateOnce(solver, initial);
        Assertions.assertEquals(expected.size(), resultArrangement.size(), "Number of stones after 1 blink does not match expected.");
        Assertions.assertEquals(expected, resultArrangement, "Arrangement after 1 blink does not match expected transformation.");
    }

    /**
     * Test the longer example given:
     * Initial: 125 17
     *
     * After 1 blink: 253000 1 7  (3 stones)
     * After 2 blinks: 253 0 2024 14168 (4 stones)
     * After 3 blinks: 512072 1 20 24 28676032 (5 stones)
     * After 4 blinks: 512 72 2024 2 0 2 4 2867 6032 (9 stones)
     * After 5 blinks: [long arrangement given,  ... ] total = ?
     * After 6 blinks: total 22 stones
     * After 25 blinks: total 55312 stones
     *
     * We can test a few of these intermediate steps.
     */
    @Test
    public void testMultiStepExample() throws IOException {
        Day11 solver = new Day11(null);

        String initial = "125 17";

        // After 1 blink: 253000 1 7
        List<Long> expectedAfter1 = Arrays.asList(253000L, 1L, 7L);
        List<Long> resultAfter1 = simulate(solver, initial, 1);
        Assertions.assertEquals(expectedAfter1, resultAfter1, "After 1 blink, arrangement mismatch.");

        // After 2 blinks: 253 0 2024 14168
        List<Long> expectedAfter2 = Arrays.asList(253L, 0L, 2024L, 14168L);
        List<Long> resultAfter2 = simulate(solver, initial, 2);
        Assertions.assertEquals(expectedAfter2, resultAfter2, "After 2 blinks, arrangement mismatch.");

        // After 3 blinks: 512072 1 20 24 28676032
        List<Long> expectedAfter3 = Arrays.asList(512072L, 1L, 20L, 24L, 28676032L);
        List<Long> resultAfter3 = simulate(solver, initial, 3);
        Assertions.assertEquals(expectedAfter3, resultAfter3, "After 3 blinks, arrangement mismatch.");

        // After 6 blinks, the puzzle states you have 22 stones total.
        List<Long> resultAfter6 = simulate(solver, initial, 6);
        Assertions.assertEquals(22, resultAfter6.size(), "After 6 blinks, expected 22 stones.");

        // After 25 blinks, puzzle states 55312 stones
        long countAfter25 = simulateCountOnly(solver, initial, 25);
        Assertions.assertEquals(55312, countAfter25, "After 25 blinks, expected 55312 stones.");

        // For your own input, after 25 blinks you had 217812 stones - you can test that as well if you know your input.
    }

    /**
     * This method would simulate a given number of blinks and return the final arrangement as a list of Longs.
     * You'll need to implement 'simulate' or adapt your Day11 code so that you can run a custom number of blinks
     * and return the final arrangement.
     *
     * If your code currently only returns the count (like your puzzle solution),
     * you might need to refactor to produce the final arrangement.
     *
     * For now, let's assume you have a method in Day11:
     *   public List<Long> simulateArrangement(String input, int blinks)
     * that returns the final arrangement.
     */
    private List<Long> simulate(Day11 solver, String input, int blinks) throws IOException {
        // Implement or call a method in Day11 that simulates `blinks` times and returns the final stones.
        // This is a placeholder. Replace with your actual call.

        // Example:
        // return solver.simulateArrangement(input, blinks);

        throw new UnsupportedOperationException("Implement simulateArrangement method in Day11 to return final arrangement.");
    }

    /**
     * Similar to simulate but for a single blink, to simplify code in the single-step test.
     */
    private List<Long> simulateOnce(Day11 solver, String input) throws IOException {
        return simulate(solver, input, 1);
    }

    /**
     * If you don't want to reconstruct the entire arrangement (which may be huge after many blinks),
     * you can test just the counts after certain known blink counts, if your main code supports it.
     *
     * Assume you have a method:
     *  public long simulateCount(String input, int blinks)
     * that only returns the count of stones after `blinks` iterations.
     *
     * Update your Day11 code to support such a method if it doesn't already.
     */
    private long simulateCountOnly(Day11 solver, String input, int blinks) throws IOException {
        // Example:
        // return solver.simulateCount(input, blinks);

        throw new UnsupportedOperationException("Implement simulateCount method in Day11 to return final stone count.");
    }
}
