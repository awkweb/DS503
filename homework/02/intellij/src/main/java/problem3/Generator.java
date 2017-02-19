package problem3;

import utils.CsvUtils;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    public Generator() {
    }

    public static void main(String[] args) throws Exception {
        System.out.println("PROBLEM 3 DATA GENERATOR");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        Scanner reader = new Scanner(System.in);  // Reading from System.in
        System.out.println("Enter a number of points (1,000,000 is around 100MB): ");
        int pointsCount = reader.nextInt();
        generateData(true, pointsCount);

        System.out.println("Enter K initial seed points: ");
        int seedCount = reader.nextInt();
        generateData(false, seedCount);

        System.out.println("Done.");
        System.out.println("");
    }

    private static void generateData(Boolean choice, int k) throws Exception {
        String fileName = choice ? "./input/problem3/points_data.csv" : "./input/problem3/k_data.csv";
        FileWriter writer = new FileWriter(fileName);

        int RECORD_COUNT = choice ? 10000000 : k;
        int MIN_VALUE = 0;
        int MAX_VALUE = 10000;

        System.out.println(String.format("Generating %d points...", k));
        for (int i=1; i<=RECORD_COUNT; i++) {
            String x = Integer.toString(getRandomIntegerBetween(MIN_VALUE, MAX_VALUE));
            String y = Integer.toString(getRandomIntegerBetween(MIN_VALUE, MAX_VALUE));
            List<String> point = new ArrayList<String>(Arrays.asList(x, y));
            CsvUtils.writeLine(writer, point);
        }

        writer.flush();
        writer.close();
    }

    private static int getRandomIntegerBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
