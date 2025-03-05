package tsp.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPSolutionFileReader {

    public List<Integer> readNumbersFromFile(String filename) {
        List<Integer> numbers = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line by whitespace and add each number to the list
                String[] parts = line.trim().split("\\s+");
                for (String part : parts) {
                    try {
                        int number = Integer.parseInt(part);
                        numbers.add(number);
                    } catch (NumberFormatException e) {
                        // If the string is not a valid integer, ignore it
                        System.err.println("Invalid number format: " + part);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return numbers;
    }
}
