package org.aoc;

import org.aoc.days.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("**************************************************");
        System.out.println("*                                                *");
        System.out.println("*   Welcome to the Advent of Christmas Code! 🎄  *");
        System.out.println("*                                                *");
        System.out.println("*   Get ready for automated solutions! ✨         *");
        System.out.println("**************************************************\n");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Ask for the day
            System.out.print("Enter the day (e.g., 1 for Day 1, or 'q' to quit): ");
            String dayInput = scanner.nextLine().trim();

            if (dayInput.equalsIgnoreCase("q")) {
                System.out.println("Goodbye! See you for the next challenge!");
                break;
            }

            try {
                int day = Integer.parseInt(dayInput);

                System.out.print("Enter the part (e.g., 1 for Part 1, or 'q' to quit): ");
                String partInput = scanner.nextLine().trim();

                if (partInput.equalsIgnoreCase("q")) {
                    System.out.println("Goodbye! See you for the next challenge!");
                    break;
                }

                int part = Integer.parseInt(partInput);

                String filePath = "src/main/resources/day" + day;
                System.out.println("\nRunning Day " + day + ", Part " + part + "...\n");
                System.out.println("Using input file: " + filePath);

                // Check if the file exists
                if (!Files.exists(Paths.get(filePath))) {
                    System.out.println("Error: Input file not found at " + filePath);
                    continue;
                }

                long result;
                switch (day) {
                    case 1:
                        result = new Day1(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 2:
                        result = new Day2(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 3:
                        result = new Day3(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 4:
                        result = new Day4(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 5:
                        result = new Day5(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 6:
                        result = new Day6(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 7:
                        result = new Day7(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 8:
                        result = new Day8(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 9:
                        result = new Day9(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 10:
                        result = new Day10(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 11:
                        result = new Day11(filePath).runPart(part);
                        System.out.println("Result: " + result);
                        break;
                    case 12:
                        result = new Day12(filePath).solvePart2();
                        System.out.println("Result: " + result);
                        break;
                    case 13:
                        result = new Day13(filePath).solvePart2();
                        System.out.println("Result: " + result);
                        break;
                    default:
                        System.out.println("Day not implemented yet.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid day and part number.");
            } catch (IOException e) {
                System.out.println("Error reading input file: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        }

        System.out.println("\n**************************************************");
        System.out.println("* Thank you for using the Advent of Christmas!  *");
        System.out.println("*   See you for the next challenge!            *");
        System.out.println("**************************************************");
    }
}
