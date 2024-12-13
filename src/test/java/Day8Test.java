import org.aoc.days.Day8;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day8Test {

    @Test
    public void testPart1ExampleInput() throws IOException {
        String testFilePath = "src/test/resources/day8_test_input.txt";
        String testInput = """
                ............
                ........0...
                .....0......
                .......0....
                ....0.......
                ......A.....
                ............
                ............
                ........A...
                .........A..
                ............
                ............
                """;

        java.nio.file.Files.writeString(java.nio.file.Paths.get(testFilePath), testInput);

        Day8 day8 = new Day8(testFilePath);
        long result = day8.runPart(1);
        assertEquals(14, result, "Expected 14 unique locations with antinodes.");
    }

    @Test
    public void testPart2ExampleInput() throws IOException {
        String testFilePath = "src/test/resources/day8_test_part2_input.txt";
        String testInput = """
                ............
                ........0...
                .....0......
                .......0....
                ....0.......
                ......A.....
                ............
                ............
                ........A...
                .........A..
                ............
                ............
                """;

        java.nio.file.Files.writeString(java.nio.file.Paths.get(testFilePath), testInput);

        Day8 day8 = new Day8(testFilePath);
        long result = day8.runPart(2);
        assertEquals(34, result, "Expected 34 unique locations with antinodes including antenna positions.");
    }
}