import java.util.List;


class Parser { // has two fields: tokens and current
    private final List<Token> tokens; // tokens is a list of tokens
    private int current = 0;
  
    public Parser(List<Token> tokens) {// constructor
      this.tokens = tokens; 
    }
  
    private Token getCurrentToken() { // getter for curr token in tokens list
      return tokens.get(current);
    }
  
    private void nextToken() { // getter for the next token in tokens list
      current++;
    }
  
    private boolean hasMoreTokens() { // checks if there are more tokens in the list
      return current < tokens.size();
    }
  
    public int parse() { // parse method
      int expression = parseExpression();
      if (hasMoreTokens()) { // if there are more tokens throw an error else return the expression
        throw new RuntimeException("Unexpected tokens remaining");
      }
      return expression; // return the expression
    }
  
    private int parseExpression() { // parseExpression method does the same as parseTerm and parseFactor for +/-
        int left = parseTerm(); // left is the result of parseTerm
        while (hasMoreTokens() && (getCurrentToken().getType().equals("PLUS") || 
                                   getCurrentToken().getType().equals("MINUS"))) {
          Token token = getCurrentToken();
          if (token.getType().equals("PLUS")) {
            nextToken();
            left = left + parseTerm();
          } else if (token.getType().equals("MINUS")) {
            nextToken();
            left = left - parseTerm();
          }
        }
        return left;
      }
  
    private int parseTerm() { // parseTerm method does the same as parseFactor for */-
        int left = parseFactor();
        while (hasMoreTokens() && (getCurrentToken().getType().equals("MULTIPLY") || 
                                   getCurrentToken().getType().equals("DIVIDE"))) {
          Token token = getCurrentToken();
          if (token.getType().equals("MULTIPLY")) {
            nextToken();
            left = left * parseFactor();
          } else if (token.getType().equals("DIVIDE")) {
            nextToken();
            left = left / parseFactor();
          }
        }
        return left;
      }
  
      private int parseFactor() { // parseFactor method
        Token token = getCurrentToken(); // get the current token
        if (token.getType().equals("NUMBER")) {
            nextToken();
            return Integer.parseInt(token.getValue());
        } else if (token.getType().equals("LEFT_PAREN")) {
            nextToken();
            int result = parseExpression();
            if (!getCurrentToken().getType().equals("RIGHT_PAREN")) {
                throw new RuntimeException("Expected closing parenthesis, found " + getCurrentToken().getValue());
            }
            nextToken();
            return result;
        } else {
            throw new RuntimeException("Unexpected token: " + token.getValue());
        }
    }
    
    }

  
  
  
  
  
  