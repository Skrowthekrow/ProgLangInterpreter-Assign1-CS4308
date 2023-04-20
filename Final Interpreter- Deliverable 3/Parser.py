from typing import List
from Lexer import Token, TokenType


class Parser: # Parser class that parses the tokens
    def __init__(self, tokens: List[Token]):
        self.tokens = tokens # token list
        self.pos = 0 # current position in the token list

    def parse(self): # parse method
        nodes = []
        while self.pos < len(self.tokens):
            node = self.parse_token()
            if node:
                nodes.append(node)
        return nodes
    def parse_function_parameters(self): # parse function parameters
        function_parameters = [] # function parameters list
        while self.current_token.value != ")":
            function_parameters.append({"type": "variable", "value": self.current_token.value})
            self.consume()
            if self.current_token.value == ",": # if the current token is a comma
                self.consume()
        self.consume()
        return function_parameters # return function parameters list

    def parse_token(self): # parse token method
        token = self.tokens[self.pos]
        self.pos += 1

        if token.token_type == TokenType.KEYWORD:   # if the token type is a keyword
            return {"type": "keyword", "value": token.value}
        elif token.token_type == TokenType.IDENTIFIER: # if the token type is an identifier
            return {"type": "variable", "value": token.value}
        elif token.token_type == TokenType.OPERATOR:    # if the token type is an operator
            return {"type": "operator", "value": token.value}
        elif token.token_type == TokenType.NUMBER: # if the token type is a number
            return {"type": "number", "value": token.value}
        elif token.token_type == TokenType.STRING: # if the token type is a string
            return {"type": "string", "value": token.value}
        return None # return none IF the token type is not a keyword, identifier, operator, number, or string
