import org.aoc.days.Day7;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day7Test {

    @Test
    void testPart1() throws IOException {
        // Create a temporary file for testing
        String testFilePath = "src/test/resources/day7_test_input.txt";

        // Test input based on the example
        String testInput = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """;

        // Write the test input to the file
        java.nio.file.Files.writeString(java.nio.file.Paths.get(testFilePath), testInput);

        // Initialize the Day7 class with the test file
        Day7 day7 = new Day7(testFilePath);

        // Test Part 1
        long result = day7.runPart(1);
        assertEquals(3749, result); // Expected sum of test values
    }

    @Test
    void testPart2() throws IOException {
        // Create a temporary file for testing
        String testFilePath = "src/test/resources/day7_test_input_part2.txt";

        // Test input based on the Part Two example
        String testInput = """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
        """;

        // Write the test input to the file
        java.nio.file.Files.writeString(java.nio.file.Paths.get(testFilePath), testInput);

        // Initialize the Day7 class with the test file
        Day7 day7 = new Day7(testFilePath);

        // Test Part 2
        long result = day7.runPart(2);

        // Verify the result matches the example in the question
        assertEquals(11387, result); // Expected total calibration result for Part Two
    }
}
