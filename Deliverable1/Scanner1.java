// second scanner attempt
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList; //import the ArrayList class
import java.util.List; //import the List class
import java.util.Arrays; //import the Arrays class


public class Scanner1 {
  public static void main(String[] args) {
    // lists of operators, keywords, variables, identifiers, and tokens
    List<String> operators = Arrays.asList("+", "-", "*", "/", "%", "&&", "||", "!", ">", ">=", "<", "<=", "==", "!=", "=", "(", ")", "{", "}", ";", "/*", "*/");
    List<String> keywords = Arrays.asList("import", "description", "symbol", "forward", "function", "global", "constants", "define", "implementations", "begin", "endfun", "call", "exit");
    List<String> variables = new ArrayList<String>();
    ArrayList<String> identifiers = new ArrayList<String>();
    ArrayList<String> tokens = new ArrayList<String>();
    final String ANSI_RED = "\u001B[31m"; //ANSI code for red
    final String ANSI_GREEN = "\u001B[32m"; //ANSI code for green
    final String ANSI_RESET = "\u001B[0m"; //ANSI code for reset
    // try catch block to catch any errors
    // will check each line for tokens and add them to the tokens list
    // will also check for repeated identifiers and add them to the identifiers list
    // will also check for repeated variables and add them to the variables list
    // will also check for operators, keywords, numbers, and identifiers
    // will also check for comments

    try {
        Scanner scanner = new Scanner(System.in); // Create a Scanner object
        System.out.print("Enter a FileName: "); // Prompt the user to enter a file name
        String filename = scanner.nextLine(); // Read user input
        File myObj = new File(filename); // Create a File object and pass the user input as the file name
        scanner.close(); // Close the scanner

        Scanner myReader = new Scanner(myObj); // Create a Scanner object and pass the File object as the parameter

        boolean inBlockComment = false; // boolean to check if the scanner is in a block comment
        while (myReader.hasNextLine()) { // while loop to check each line
            String line = myReader.nextLine();
            String[] lineTokens = line.split("\\s+"); // split the line into tokens

            for (String token : lineTokens) { // for loop to check each token
                if (inBlockComment) { // if the scanner is in a block comment
                    if (token.endsWith("*/")) { // if the token ends with a block comment
                        inBlockComment = false; // set the boolean to false
                    } 
                    continue; // continue to the next token
                }

                if (token.startsWith("//")) { // if the token starts with a line comment
                    break;
                } else if (token.startsWith("/*")) { // if the token starts with a block comment
                    inBlockComment = true;
                } else if (operators.contains(token)) { // if the token is an operator
                    tokens.add(ANSI_GREEN + token + " is an operator." + ANSI_RESET);
                } else if (keywords.contains(token)) { // if the token is a keyword
                    tokens.add(ANSI_GREEN + token + " is a keyword." + ANSI_RESET);
                } else if (isNumeric(token)) { // if the token is a number
                    tokens.add(ANSI_GREEN + token + " is a number." + ANSI_RESET);
                } else if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) { // if the token is an identifier using regex
                    if (identifiers.contains(token)) {  // if the token is an identifier
                        tokens.add(ANSI_GREEN + token + " is a repeated identifier." + ANSI_RESET);
                    } else { // if the token is not an identifier
                        identifiers.add(token);
                        tokens.add(ANSI_GREEN + token + " is an identifier." + ANSI_RESET);
                    }
                } else if (token.matches("\".*\"")) {   // if the token is a string literal using regex
                    tokens.add(ANSI_GREEN + token + " is a string literal." + ANSI_RESET);
                } else if (token.length() == 1 && Character.isLetter(token.charAt(0))) {
                    if (variables.contains(token)) {  // if the token is a variable
                        tokens.add(ANSI_GREEN + token + " is a repeated variable." + ANSI_RESET);
                    } else {  // if the token is not a variable
                        variables.add(token);
                        tokens.add(ANSI_GREEN + token + " is a variable." + ANSI_RESET);
                    }
                } else {  // if the token is not an operator, keyword, number, identifier, or variable
                    tokens.add(ANSI_RED + token + " is an unknown token." + ANSI_RESET);
                }
            }
        }

        for (String token : tokens) { // for loop to print each token
            System.out.println(token);
        }

        myReader.close();
    } catch (FileNotFoundException e) { // catch block to catch any errors
        System.out.println("An error occurred."); // print error message
        e.printStackTrace(); // print stack trace
    }
}

private static boolean isNumeric(String strNum) { // method to check if a token is a number
    if (strNum == null) {
        return false;
    }
    try {
        Double.parseDouble(strNum); // try to parse the token as a double
    } catch (NumberFormatException nfe) {
        return false;
    }
    return true;
}
}
// test file: arduino_ex1.scl