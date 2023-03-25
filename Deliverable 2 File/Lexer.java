import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Lexer {
    private String[] keywords = {"import", "description", "symbol", "forward", "function", "global", "constants", "define", "implementations",
            "begin", "endfun", "call", "exit"}; //TODO: add all keywords
    private String[] operators = {"+", "-", "*", "/", "%", "&&", "||", "!", ">", ">=", "<", "<=", "==", "!=", "=", "(", ")", "{", "}", ";",
            "/*", "*/"}; //TODO: add /* and */ to operators
    private Set<String> declaredIdentifiers = new HashSet<>();

    public boolean identifierExists(String identifier) {
        return declaredIdentifiers.contains(identifier);
    }

    public List<Token> lex(String fileName) {
        List<Token> tokens = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            boolean inComment = false;
            boolean inDescription = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
    
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
    
                    if (inDescription) {
                        if (i + 1 < line.length() && c == '*' && line.charAt(i + 1) == '/') {
                            inDescription = false;
                            i++;
                        }
                    } else if (inComment) {
                        if (i + 1 < line.length() && c == '*' && line.charAt(i + 1) == '/') {
                            inComment = false;
                            i++;
                        }
                    } else {
                        if (i + 1 < line.length() && c == '/' && line.charAt(i + 1) == '/') {
                            break;
                        } else if (i + 1 < line.length() && c == '/' && line.charAt(i + 1) == '*') {
                            if (!inDescription) {
                                inComment = true;
                            }
                            i++;
                        } else {
                            if (Character.isDigit(c)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(c);
                                while (i + 1 < line.length() && Character.isDigit(line.charAt(i + 1))) {
                                    sb.append(line.charAt(i + 1));
                                    i++;
                                }
                                tokens.add(new Token(TokenType.NUMBER, sb.toString()));
                            } else if (Character.isLetter(c)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append(c);
                                while (i + 1 < line.length() && (Character.isLetterOrDigit(line.charAt(i + 1)) || line.charAt(i + 1) == '_')) {
                                    sb.append(line.charAt(i + 1));
                                    i++;
                                }
                                String identifier = sb.toString();
                                if (Arrays.asList(keywords).contains(identifier)) {
                                    if (identifier.equals("description")) {
                                        inDescription = true;
                                    }
                                    tokens.add(new Token(TokenType.KEYWORD, identifier));
                                } else {
                                    if (i + 1 < line.length() && line.charAt(i + 1) == ' ' && line.substring(i + 2).startsWith("of type ")) {
                                        tokens.add(new Token(TokenType.VARIABLE, identifier));
                                        i += 9;
                                        while (i + 1 < line.length() && !Character.isWhitespace(line.charAt(i + 1))) {
                                            i++;
                                        }
                                    } else {
                                        tokens.add(new Token(TokenType.IDENTIFIER, identifier));
                                        declaredIdentifiers.add(identifier);
                                    }
                                }
                            } else if (Arrays.asList(operators).contains(String.valueOf(c))) {
                                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c)));
                            }
                        }
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        return tokens;
    }
}