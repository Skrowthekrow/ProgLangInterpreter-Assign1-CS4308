import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int index; // index of the next token to be parsed

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.index = 0; // initialize the index to 0
    }

    public void begin() {
        while (hasNextToken()) {
            Token token = getNextToken();
            System.out.println("Next token is: " + token.getType().ordinal() + " Next lexeme is: " + token.getValue() + " entering " + token.getType().toString());
        } // print the token type, token value, and the name of the method that is being called
    }

    private boolean hasNextToken() { // check if there is a next token
        return index < tokens.size();
    }

    private Token getNextToken() { // Get the next token from the list of tokens
        return tokens.get(index++);
    }

    public static void main(String[] args) { // main method
        Lexer lexer = new Lexer(); // create a new instance of the Lexer class
        List<Token> tokens = lexer.lex("arduino_ex1.scl"); // call the lex method on the instance of the Lexer class
        Parser parser = new Parser(tokens); // create a new instance of the Parser class
        parser.begin(); // call the begin method on the instance of the Parser class
    }
}

