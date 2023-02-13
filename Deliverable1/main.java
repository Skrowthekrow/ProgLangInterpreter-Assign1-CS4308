

import java.util.Scanner;
import java.util.List;
// code can handle + - * / and ()

public class main {
    public static void main(String[] args) { // main method
        String input = "(2+5)*2/7"; // input is a string and is read from the user no spaces allowed atm.
        Lexer lexer = new Lexer(input); // create a new lexer object
        List<Token> tokens = lexer.tokenize(); // create a list of tokens
        Parser parser = new Parser(tokens); // create a new parser object
        int result = parser.parse(); // parse the tokens
        System.out.println(result); // print the result
       
   }
}
