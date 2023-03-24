import java.util.List; // import java.util.List;

public class Parser { // Parser.java
    private List<Token> tokens;
    private int index; // index of the next token to be parsed

    public Parser(List<Token> tokens) {
        this.tokens = tokens; // this.tokens = tokens;
        this.index = 0;     //initialize the index to 0
    }

    public void parse() {
        while (hasNextToken()) { // while (hasNextToken()) {
            Token token = getNextToken();
            System.out.println("Next token is: " + token.getType().ordinal() + " Next lexeme is: " + token.getValue() + " entering " + token.getType().toString());
        }    //print the token type, token value, and the name of the method that is being called
    }

    private boolean hasNextToken() { // check if there is a next token
        return index < tokens.size();
    }

    private Token getNextToken() { // Get the next token from the list of tokens
        return tokens.get(index++);
    }
}
