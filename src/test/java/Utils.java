import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

public class Utils {

    public static final String filename = "build.txt";
    private static Scanner scanner;
    private static int length = 0;

    public static double getBuildNumber(String filename) throws IOException {

        File myObj = new File(filename);
        Scanner myReader = new Scanner(myObj);
        double myBuildNum = 0.0;

        try {

            while (myReader.hasNextLine()) {

                String data = myReader.nextLine();
                myBuildNum = Float.parseFloat(data);
                System.out.println("Original Build Number:" +myBuildNum);

            }

        }
        catch (Exception e) {
            System.out.println("Sup Yo! " +e.getMessage());
            e.printStackTrace();
        }
        finally {

            myReader.close();
        }
        myBuildNum+=0.1;
        myBuildNum = Double.parseDouble(new DecimalFormat("#.#").format(myBuildNum));
        System.out.println("New build Number is: " +myBuildNum);
        writeToFile("build.txt", myBuildNum);
        return myBuildNum;

    }

    public static void writeToFile(String filename, double buildNumber) throws IOException {

        BufferedWriter out = new BufferedWriter(new FileWriter(filename));

        try {

            out.write(String.valueOf(buildNumber));
            System.out.println("File created successfully");
        }
        catch (IOException e) {

            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {

            out.close();
        }
    }

        public static String changeEnv() {

            String[] myEnvironments = {"QA", "DEV", "UAT", "PROD"};
            int min = 0;

            Random rand = new Random();
            // int randomNumId = rand.nextInt((max - min) + 1) + min;
            int randomNumId = rand.nextInt((myEnvironments.length - min) + 1) + min;
            System.out.println("Environment is: " + myEnvironments[randomNumId]);
            return myEnvironments[randomNumId];

        }

            public static String genHash (int length){

                // create a string of lowercase characters and numbers
                String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
                String numbers = "0123456789";
                // concatentate  strings
                String alphaNumeric = lowerAlphabet + numbers;
                // create random string builder object
                StringBuilder sb = new StringBuilder();
                // create an object of Random class
                Random random = new Random();

                for (int i = 0; i < length; i++) {
                    // generate random index number
                    int index = random.nextInt(alphaNumeric.length());
                    // get character specified by index
                    // from the string
                    char randomChar = alphaNumeric.charAt(index);
                    // append the character to string builder
                    sb.append(randomChar);
                }
                String randomString = sb.toString();
                System.out.println("Random String is: " + randomString);
                return randomString;
            }

        }