// second scanner attempt
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Set;
import java.util.ArrayList; //import the ArrayList class
import java.util.List; //import the List class
import java.util.Arrays; //import the Arrays class
import java.util.HashSet;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


//THINGS TO DO: Generate a JSON File with list of tokens
//              Decided on using a txt file for output----> DONE
//              Need to maybe handle the ouput so it looks nicer ---> INCOMPLETE
//              Create Subset Grammar for the language ---> INCOMPLETE
//              Provide I/O files and sourcecode ---> INCOMPLETE
//              Solve variable issue ---> INCOMPLETE
//              Added new keywords ---> DONE


public class Scanner1 {
  public static void main(String[] args) {
    // lists of operators, keywords, variables, identifiers, and tokens
    List<String> operators = Arrays.asList("+", "-", "*", "/", "%", "&&", "||", "!", ">", ">=", "<", "<=", "==", "!=", "=", "(", ")", "{", "}", ";", "//", "/*", "*/");
    List<String> keywords = Arrays.asList(
    "import", "description", "symbol", "forward", "function", "global", "constants", "define", 
    "implementations", "begin", "endfun", "call", "exit", "struct", "definetype", "increment", 
    "decrement", "set", "forward", "declarations", "pointer", "parameters", "is", "of", "type", 
    "integer", "void", "double", "long", "short", "float", "unsigned", "byte", "array", "structures", 
    "return", "input", "endfor", "then", "endif", "call", "using", "endwhile", "while", "destroy", "exit"
    );
    Set<String> reservedWords = new HashSet<>(Arrays.asList("SCL", "REAL", "INT", "BOOL", "CHAR", "STRING", "ARRAY", "TRUE", "FALSE", "OF", "IF", "THEN", "ELSE", "WHILE", "DO", "BEGIN", "END", "READ", "WRITE", "VAR"));
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
    String[] lineTokens = line.split("\\s*(\\s|,|;|\\(|\\)|\\r?\\n)\\s*"); // split the line into tokens, handling commas and spaces

    for (String token : lineTokens) { // for loop to check each token
        if (token.isEmpty()) { // skip empty tokens
            continue;
        }
        if (inBlockComment) { // if the scanner is in a block comment
            if (token.endsWith("*/")) { // if the token ends with a block comment
                inBlockComment = false; // set the boolean to false
            } 
            continue; // continue to the next token
        }
        if (token.startsWith("//")) { // if the token starts with a line comment
            break;
        } else if (token.startsWith("description")) { // if the token starts with a block comment
            inBlockComment = true;
            if (!token.startsWith("description")) { // if the block comment does not start with "description"
                continue; // skip to the next token
            }
        } else if (operators.contains(token)) { // if the token is an operator
            tokens.add(ANSI_GREEN + token + " is an operator." + ANSI_RESET);
        } else if (keywords.contains(token)) { // if the token is a keyword
            tokens.add(ANSI_GREEN + token + " is a keyword." + ANSI_RESET);
        } else if (isNumeric(token)) { // if the token is a number
            tokens.add(ANSI_GREEN + token + " is a number." + ANSI_RESET);
        } else if (token.startsWith("\"") && token.endsWith("\"")) { // if the token is a string
            tokens.add(ANSI_GREEN + token + " is a string." + ANSI_RESET);
        } else if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) { // if the token is an identifier using regex
            if (identifiers.contains(token)) {  // if the token is a repeated identifier
                tokens.add(ANSI_GREEN + token + " is a repeated identifier." + ANSI_RESET);
            } else { // if the token is not a repeated identifier
                identifiers.add(token);
                if (keywords.contains(token)) { // if the token is a keyword
                    tokens.add(ANSI_GREEN + token + " is a keyword." + ANSI_RESET);
                } else if (reservedWords.contains(token)) { // if the token is a reserved word that is not a keyword
                    tokens.add(ANSI_GREEN + token + " is a reserved word." + ANSI_RESET);
                } else { // if the token is a variable
                    if (variables.contains(token)) { // if the token is a repeated variable
                        tokens.add(ANSI_GREEN + token + " is a repeated variable." + ANSI_RESET);
                    } else {
                        variables.add(token);
                        tokens.add(ANSI_GREEN + token + " is a variable." + ANSI_RESET);
                    }
                }
            }
        } else { // if the token is none of the above
            tokens.add(ANSI_RED + token + " is not a valid token." + ANSI_RESET);
        }
    }
}


    
    myReader.close(); // Close the scanner

        String fileName = "output.txt";

        OutputText.writeText(tokens, fileName);

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

public class OutputText {
    public static void writeText(List<String> tokens, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String token : tokens) {
                writer.write(token);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

}
// test file: arduino_ex1.scl