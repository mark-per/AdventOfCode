//import org.aoc.Main;
//import org.junit.jupiter.api.*;
//import java.io.*;
//import java.nio.file.*;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//class MainTest {
//
//    private final PrintStream originalOut = System.out;
//    private final InputStream originalIn = System.in;
//    private ByteArrayOutputStream testOut;
//    private ByteArrayInputStream testIn;
//
//    @BeforeEach
//    void setUpStreams() {
//        testOut = new ByteArrayOutputStream();
//        System.setOut(new PrintStream(testOut));
//    }
//
//    @AfterEach
//    void restoreStreams() {
//        System.setOut(originalOut);
//        System.setIn(originalIn);
//    }
//
//    private void provideInput(String data) {
//        testIn = new ByteArrayInputStream(data.getBytes());
//        System.setIn(testIn);
//    }
//
//    private String getOutput() {
//        return testOut.toString();
//    }
//
//    @Test
//    void testMainWithValidDayAndPart() throws IOException {
//        // Set up test input file for Day 7
//        String testFilePath = "src/main/resources/day7";
//        Files.writeString(Paths.get(testFilePath), "Sample input data");
//
//        // Simulate user input: Day 7, Part 1, and quit
//        provideInput("7\n1\nq\n");
//
//        // Run the Main class
//        Main.main(new String[]{});
//
//        // Capture and verify output
//        String output = getOutput();
//        assertTrue(output.contains("Running Day 7, Part 1..."));
//        assertTrue(output.contains("Using input file: src/main/resources/day7"));
//        assertTrue(output.contains("Result:")); // Verify if result is printed
//    }
//
//    @Test
//    void testMainWithMissingFile() {
//        // Simulate user input: Day 7, Part 1, and quit
//        provideInput("19\n1\nq\n");
//
//        // Run the Main class
//        Main.main(new String[]{});
//
//        // Capture and verify output
//        String output = getOutput();
//        assertTrue(output.contains("Error: Input file not found"));
//    }
//
//    @Test
//    void testMainWithInvalidDayInput() {
//        // Simulate user input: Invalid day and quit
//        provideInput("invalid\nq\n");
//
//        // Run the Main class
//        Main.main(new String[]{});
//
//        // Capture and verify output
//        String output = getOutput();
//        assertTrue(output.contains("Invalid input. Please enter a valid day and part number."));
//    }
//}
