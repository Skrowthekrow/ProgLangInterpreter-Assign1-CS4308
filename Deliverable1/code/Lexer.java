import java.util.ArrayList;
import java.util.List;


class Token { // has two fields: type and value
    private final String type;
    private final String value;
  
    public Token(String type, String value) { // constructor
      this.type = type;
      this.value = value;
    }
  
    public String getType() { // getter
      return type;
    }
  
    public String getValue() { // getter
      return value;
    }
  }

class Lexer { // has two fields: input and current
  private final String input; // input is a string
  private int current = 0;

  public Lexer(String input) { // constructor
    this.input = input;
  }

  public List<Token> tokenize() {
    List<Token> tokens = new ArrayList<>();
    int i = 0;
    while (i < input.length()) {
      char c = input.charAt(i);
      if (c >= '0' && c <= '9') {
        int j = i;
        while (j < input.length() && input.charAt(j) >= '0' && input.charAt(j) <= '9') {
          j++;
        }
        tokens.add(new Token("NUMBER", input.substring(i, j)));
        i = j;
      } else if (c == '+') {
        tokens.add(new Token("PLUS", "+"));
        i++;
      } else if (c == '-') {
        tokens.add(new Token("MINUS", "-"));
        i++;
      } else if (c == '*') {
        tokens.add(new Token("MULTIPLY", "*"));
        i++;
      } else if (c == '/') {
        tokens.add(new Token("DIVIDE", "/"));
        i++;
      } else if (c == '(') {
        tokens.add(new Token("LEFT_PAREN", "("));
        i++;
      } else if (c == ')') {
        tokens.add(new Token("RIGHT_PAREN", ")"));
        i++;
      } else {
        throw new RuntimeException("Unexpected character: " + c);
      }
    }
    return tokens;
  }
  
}

