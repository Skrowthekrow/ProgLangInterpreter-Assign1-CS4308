import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class TestLexer {
    private String[] keywords = {"import", "description", "symbol", "forward", "function", "global", "constants", "define", "implementations",
            "begin", "endfun", "call", "exit"}; //TODO: add all keywords
    private String[] operators = {"+", "-", "*", "/", "%", "&&", "||", "!", ">", ">=", "<", "<=", "==", "!=", "=", "(", ")", "{", "}", ";",
            "/*", "*/"}; //TODO: add /* and */ to operators
    
    public List<Token> lex(String fileName) { //throws FileNotFoundException 
        List<Token> tokens = new ArrayList<>(); //list of tokens
        try {   
            Scanner scanner = new Scanner(new File(fileName)); //throws FileNotFoundException
            while (scanner.hasNextLine()) { //while there is a line to read
                String line = scanner.nextLine(); //read the line
                boolean inComment = false;  //boolean to check if we are in a comment
                for (int i = 0; i < line.length(); i++) { //for each character in the line
                    char c = line.charAt(i); //get the character at the current index
                    if (inComment) { //if we are in a comment
                        if (i + 1 < line.length() && c == '*' && line.charAt(i + 1) == '/') {
                            inComment = false;
                            i++;
                        }
                    } else { //if we are not in a comment
                        if (i + 1 < line.length() && c == '/' && line.charAt(i + 1) == '/') { //if we are at the start of a line comment
                            break;
                        } else if (i + 1 < line.length() && c == '/' && line.charAt(i + 1) == '*') { //if we are at the start of a block comment
                            inComment = true;
                            i++;
                        } else if (Character.isDigit(c)) { //if we are at the start of a number
                            StringBuilder sb = new StringBuilder(); //create a string builder
                            sb.append(c);
                            while (i + 1 < line.length() && Character.isDigit(line.charAt(i + 1))) { //while the next character is a digit
                                sb.append(line.charAt(i + 1));
                                i++;
                            }
                            tokens.add(new Token(TokenType.NUMBER, sb.toString())); //add the number to the list of tokens
                        } else if (Character.isLetter(c)) { //if we are at the start of an identifier
                            StringBuilder sb = new StringBuilder(); //create a string builder
                            sb.append(c);
                            while (i + 1 < line.length() && (Character.isLetterOrDigit(line.charAt(i + 1)) || line.charAt(i + 1) == '_')) {
                                sb.append(line.charAt(i + 1)); //while the next character is a letter, digit, or underscore
                                i++;
                            }
                            String identifier = sb.toString(); //get the identifier
                            if (Arrays.asList(keywords).contains(identifier)) {     //if the identifier is a keyword
                                tokens.add(new Token(TokenType.KEYWORD, identifier));   //add the keyword to the list of tokens
                            } else {
                                tokens.add(new Token(TokenType.IDENTIFIER, identifier));    //add the identifier to the list of tokens
                            }
                        } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '>' || c == '<' || c == '=' || c == '!' || c == '&' || c == '|' || c == '(' || c == ')' || c == '{' || c == '}' || c == ',' || c == ';' || c == '[' || c == ']') {
                            //if the character is an operator
                            tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c)));
                        }
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {     //if the file is not found
            System.out.println("File not found");   //print an error message
        }
        return tokens; //return the list of tokens
    }
    

    public static void main(String[] args) { //main method
        TestLexer lexer = new TestLexer(); //create a new instance of the TestLexer class
        List<Token> tokens = lexer.lex("test3.scl"); //call the lex method on the instance of the TestLexer class
        Parser parser = new Parser(tokens); //create a new instance of the Parser class
        parser.parse(); //call the parse method on the instance of the Parser class
    }
}