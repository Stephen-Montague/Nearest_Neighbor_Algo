/*
 * Stephen Montague
 * Programming Fundamentals
 * Spring 2019 - Term 2
 * Program 3
 *
 * Summary:
 *     Compares a set of test data to a set of training data, and
 *     predicts a label based on the k-Nearest Neighbor algorithm.
 *
 *     In other words, if a test flower has petal 1 by 4 cm and sepal 2 by 2 cm,
 *     this program will check which flower in the training data is the closest match,
 *     predict the flower id, then compare this prediction to a provided correct result
 *     to self-score the accuracy of predictions.
 *
 * Program Notes:
 *     1. Accepts a .csv file of any length, however, only the first 75 lines are read.
 *     To change total number of samples, change 'final int NUM_SAMPLES' as desired.
 *
 *     2. The input files should have 4 type double attributes and 1 String identifier.
 *
 *     3. Imperfect data [missing fields, wrong data type] may affect the
 *     accuracy of predictions, however, the program should still run successfully.
 *
 *     4. The main method manually closes Scanner resource 'System.in' before exit.
 *     No 'try-with-resources' is used since this would block successful recursion
 *     from the catch block after an exception, since 'System.in' can only run once.
 *     If this class is used in a larger program, just keep the Scanner stream open.
 *
 *     5. Program is kept to a single java class as per the coursework specification.
*/

package week6.program3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NearestNeighbor {
    private static final int NUM_SAMPLES = 75;
    private static final int NUM_ATTRIBUTES = 4;
    private static double[][] trainingData = new double[NUM_SAMPLES][NUM_ATTRIBUTES];
    private static String[] trainingID = new String[NUM_SAMPLES];
    private static double[][] testData = new double[NUM_SAMPLES][NUM_ATTRIBUTES];
    private static String[] prediction = new String[NUM_SAMPLES];
    private static String[] trueID = new String[NUM_SAMPLES];
    private static double accuracy;

    public static void main(String[] args) {
        try {
            Scanner inputFilename = new Scanner(System.in);

            // Normal flow of program
            printHeader();
            readTrainingData(inputFilename);
            readTestData(inputFilename);
            setPrediction();
            setAccuracy();
            printResults();

            inputFilename.close();  // Also closes 'System.in' (See Program Note 4)

        } catch (FileNotFoundException e1) {
            System.out.println("\nError: File not found. Please try again.\n");
            resetVariables();
            NearestNeighbor.main(null);

        } catch (NoSuchElementException e2) {
            System.out.println("\nError: Insufficient lines in data file." +
                    "\nPlease ensure data files are at least " + NUM_SAMPLES + " lines long.\n");
            resetVariables();
            NearestNeighbor.main(null);

        } catch (ArrayIndexOutOfBoundsException e3) {
            System.out.println("\nError: Incompatible format. Sample has too many attributes." +
                    "\nPlease ensure data files have no more than " + NUM_ATTRIBUTES + " attributes per sample.\n");
            resetVariables();
            NearestNeighbor.main(null);
        }
    }

    private static void printHeader() {  // As per design doc
        System.out.println("Programming Fundamentals");
        System.out.println("NAME: Stephen Montague");
        System.out.println("PROGRAMMING ASSIGNMENT 3\n");
    }

    private static void readTrainingData(Scanner inputFilename) throws FileNotFoundException {
        System.out.print("Enter the name of the training file: ");
        String trainingFile = inputFilename.nextLine();
        try (Scanner scan = new Scanner(new File(trainingFile))) {
            scan.useDelimiter(",");
            // Read CSV training file to appropriate data structures
            for (int i = 0; i < NUM_SAMPLES; i++) {
                for (int j = 0; scan.hasNextDouble(); j++) {
                    trainingData[i][j] = Double.parseDouble(scan.next());
                }
                scan.skip("(,+)?"); // Skip commas, if any
                trainingID[i] = scan.nextLine();
            }
        }
    }

    private static void readTestData(Scanner inputFilename) throws FileNotFoundException {
        System.out.print("Enter the name of the test file: ");
        String testFile = inputFilename.nextLine();
        try (Scanner scanTest = new Scanner(new File(testFile))) {
            scanTest.useDelimiter(",");
            // Read CSV test file to appropriate data structures
            for (int i = 0; i < NUM_SAMPLES; i++) {
                for (int j = 0; scanTest.hasNextDouble(); j++) {
                    testData[i][j] = Double.parseDouble(scanTest.next());
                }
                scanTest.skip("(,+)?");
                trueID[i] = scanTest.nextLine();
            }
        }
    }

    private static void setPrediction() {
        /* Label each test sample (predict flower name) based on the calculation of
         * minimum distance between each test sample and the closest training sample.
         */
        double minDist;
        int minDistIndex;
        double nextCase;

        for (int i = 0; i < NUM_SAMPLES; i++) {
            minDist = setDistance(trainingData[0], testData[i]);
            minDistIndex = i;
            for (int j = 1; j < NUM_SAMPLES; j++) {
                nextCase = setDistance(trainingData[j], testData[i]);
                if (nextCase < minDist) {
                    minDist = nextCase;
                    minDistIndex = j;
                }
            }
            prediction[i] = trainingID[minDistIndex];
        }
    }

    private static double setDistance(double[] trainingData, double[] testData) {
        /* Unpack parameters into 8 variables, then calculates distance formula.
         * Abbreviations:
         *   Sepal is 's', Petal is 'p';
         *   Length is 'L', Width is 'W';
         *   Training data is 'x'; Test data is 'y'
         */
        double sL_x = trainingData[0];
        double sW_x = trainingData[1];
        double pL_x = trainingData[2];
        double pW_x = trainingData[3];
        double sL_y = testData[0];
        double sW_y = testData[1];
        double pL_y = testData[2];
        double pW_y = testData[3];
        double distance;

        distance = Math.sqrt(Math.pow((sL_x - sL_y), 2) + Math.pow((sW_x - sW_y), 2)
                + Math.pow((pL_x - pL_y), 2) + Math.pow((pW_x - pW_y), 2));

        return distance;
    }

    private static void setAccuracy() {
        // Calculate the accuracy of predicted labels.
        int count = 0;
        for (int i = 0; i < NUM_SAMPLES; i++) {
            if (prediction[i].equalsIgnoreCase(trueID[i])) {
                count++;
            }
        }
        accuracy = (double) count / NUM_SAMPLES;
    }

    private static void printResults() {
        // Print results as specified in design doc
        System.out.println("\nEX#: TRUE LABEL, PREDICTED LABEL");
        for (int i = 0; i < NUM_SAMPLES; i++) {
            System.out.println((i + 1) + ": " + trueID[i] + " " + prediction[i]);
        }
        System.out.println("ACCURACY: " + accuracy);
    }

    private static void resetVariables() {
        // Reset static variables after any exception
        for (int i = 0; i < NUM_SAMPLES; i++) {
            for (int j = 0; j < NUM_ATTRIBUTES; j++) {
                trainingData[i][j] = 0;
                testData[i][j] = 0;
            }
            trainingID[i] = "";
            prediction[i] = "";
            trueID[i] = "";
        }
        accuracy = 0;
    }
}
