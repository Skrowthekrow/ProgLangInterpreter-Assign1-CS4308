import re
from enum import Enum
from typing import List

class TokenType(Enum): # Enum is a class that represents a group of constants
    KEYWORD = 0
    IDENTIFIER = 1
    OPERATOR = 2
    NUMBER = 3
    UNKNOWN = 4
    VARIABLE = 5
    STRING = 6

class Token: # Token is a class that represents a token
    def __init__(self, token_type: TokenType, value: str): # self is the object itself
        self.token_type = token_type # self.token_type is the attribute of the object
        self.value = value # self.value is the attribute of the object

class Lexer:
    def __init__(self):
        self.keywords = [ # keywords is an attribute of the object
            "import", "description", "symbol", "forward", "function", "global", "constants", "define", "implementations",
            "begin", "endfun", "call", "exit", "set", "return", "while", "if", "else", "display"
        ]
        self.operators = [ # operators is an attribute of the object
            "+", "-", "*", "/", "%", "&&", "||", "!", ">", ">=", "<", "<=", "==", "!=", "=", "(", ")", "{", "}", ";",
            "/*", "*/", ":", ","
        ]
        self.declared_identifiers = set() # declared_identifiers is an attribute of the object

    def lex(self, file_name: str) -> List[Token]: # lex is a method of the object
        tokens = [] # token list to hold the lexed tokens
        try: # try to open the file
            with open(file_name, 'r') as file:
                in_comment = False
                in_description = False
                for line in file.readlines():
                    i = 0
                    while i < len(line):
                        c = line[i]
                        if in_description: # if in a description
                            if i + 1 < len(line) and c == '*' and line[i + 1] == '/':
                                in_description = False
                                i += 1
                        elif in_comment: # if in a comment
                            if i + 1 < len(line) and c == '*' and line[i + 1] == '/':
                                in_comment = False
                                i += 1
                        else: # if not in a comment or description
                            if i + 1 < len(line) and c == '/' and line[i + 1] == '/':
                                break
                            elif i + 1 < len(line) and c == '/' and line[i + 1] == '*':
                                if not in_description:
                                    in_comment = True
                                i += 1
                            elif c.isdigit(): # if the character is a digit
                                number = re.match(r'\d+', line[i:]).group()
                                tokens.append(Token(TokenType.NUMBER, number)) # add the number to the token list
                                i += len(number) - 1 # increment the index by the length of the number
                            elif c.isalpha() or c == '_': # if the character is a letter or underscore
                                identifier = re.match(r'\w+', line[i:]).group() # get the identifier
                                if identifier in self.keywords: # if the identifier is a keyword
                                    if identifier == "description": # if the keyword is description
                                        in_description = True # set the description flag to true
                                    tokens.append(Token(TokenType.KEYWORD, identifier)) # add the keyword to the token list
                                else:
                                    tokens.append(Token(TokenType.IDENTIFIER, identifier)) # add the identifier to the token list
                                    self.declared_identifiers.add(identifier) # add the identifier to the declared identifiers set
                                i += len(identifier) - 1
                            elif c in self.operators: # if the character is an operator
                                if c == "<" and i + 1 < len(line) and line[i + 1] == "=":
                                    tokens.append(Token(TokenType.OPERATOR, "<=")) # add the operator to the token list
                                    i += 1 # might need to add more operators
                                elif c == ">" and i + 1 < len(line) and line[i + 1] == "=":
                                    tokens.append(Token(TokenType.OPERATOR, ">="))
                                    i += 1
                                elif c == "=" and i + 1 < len(line) and line[i + 1] == "=":
                                    tokens.append(Token(TokenType.OPERATOR, "=="))
                                    i += 1
                                elif c == "!" and i + 1 < len(line) and line[i + 1] == "=":
                                    tokens.append(Token(TokenType.OPERATOR, "!="))
                                    i += 1

                                else:
                                    tokens.append(Token(TokenType.OPERATOR, c)) # add the operator to the token list
                            elif c == '"':
                                string_start = i
                                i += 1
                                while i < len(line) and line[i] != '"':
                                    i += 1
                                string_end = i
                                string_value = line[string_start + 1:string_end] # get the string value
                                tokens.append(Token(TokenType.STRING, string_value)) # add the string to the token list
                        i += 1
        except FileNotFoundError: # if the file is not found
            print(f"Error: File '{file_name}' not found.")
        return tokens
    def tokenize(self, source_code): # tokenize is a method of the object
        tokens = [] # token list to hold the lexed tokens

        while self.current_position < len(source_code): # while the current position is less than the length of the source code
            current_char = source_code[self.current_position] # get the current character

            if current_char.isspace():
                self.consume_whitespace(source_code) # consume the whitespace
            elif current_char.isalpha():
                token = self.consume_identifier(source_code) # consume the identifier
                tokens.append(token)
            elif current_char.isdigit():
                token = self.consume_number(source_code)    # consume the number
                tokens.append(token)
            elif current_char == '"':
                token = self.consume_string(source_code) # consume the string
                tokens.append(token)
            elif current_char in {'(', ')', ',', '='}:
                token = self.consume_operator(source_code)
                tokens.append(token)
                if current_char == '(' and tokens[-2].value in {"integer", "float", "boolean", "string"}:
                    tokens.pop(-2)
            else:
                self.raise_error(f"Unexpected character: {current_char}") # raise an error if the character is not expected

        return tokens

