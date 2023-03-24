// Token.java
public class Token {
    private TokenType type; // type of the token
    private String value; // value of the token

    public Token(TokenType type, String value) { // Token.java
        this.type = type;
        this.value = value;
    }

    public TokenType getType() { // get the type of the token
        return type;
    }

    public void setType(TokenType type) { // set the type of the token
        this.type = type;
    }

    public String getValue() { // get the value of the token
        return value;
    }

    public void setValue(String value) {    // set the value of the token
        this.value = value;
    }
}
