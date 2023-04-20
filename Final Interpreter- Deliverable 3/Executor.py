import json
from Lexer import Lexer
from Parser import Parser

class Executor:
    def __init__(self, ast):
        self.ast = ast

    def execute(self):
        python_code = []
        i = 0
        functions_defined = set()
        while i < len(self.ast):
            if self.ast[i]['type'] == 'keyword' and self.ast[i]['value'] == 'function':
                function_declaration = self.process_function_declaration(i)
                function_name = function_declaration.split()[1]
                if function_name in functions_defined:
                    i += 1
                    continue
                else:
                    functions_defined.add(function_name)
                python_code.append(function_declaration)
                i += 1
            elif self.ast[i]['type'] == 'operator' and self.ast[i]['value'] == ';':
                i += 1
            else:
                line = self.line_processor(i)
                if line:
                    python_code.append("  " + line)
                i += 1  # Move incrementing 'i' here

            # Skip extra whitespace
            while i < len(self.ast) and self.ast[i]['type'] == 'operator' and self.ast[i]['value'] == ';':
                i += 1
            return "".join(python_code)



    def process_function_declaration(self, index):  # index is the index of the 'function' keyword
        function_name = self.ast[index + 1]['value']
        params = [] # List of parameter names
        i = index + 3

        while self.ast[i]['type'] != 'keyword' or self.ast[i]['value'] != 'begin':
            if self.ast[i]['type'] == 'variable' and self.ast[i - 1]['type'] != 'variable' and self.ast[i - 1]['value'] != ':':
                param_name = self.ast[i]['value']
                params.append(param_name)
                i += 1
            else:
                i += 1

        function_declaration = f"def {function_name}({', '.join(params)}):"
        i += 1

        while i < len(self.ast) and (self.ast[i]['type'] != 'keyword' or self.ast[i]['value'] != 'endfun'):
            line = self.line_processor(i)  # Get the updated 'i' from line_processor (no need for tuple unpacking)
            if line:
                function_declaration += "\n  " + line # Add 2 spaces to the beginning of the line
            i += 1 # Move incrementing 'i' here

        return function_declaration # Return the function declaration


    def line_processor(self, index): # index is the index of the first token in the line
        line = "" # Line to be returned
        if self.ast[index]['type'] == 'keyword': # If the first token is a keyword
            if self.ast[index]['value'] == 'set':
                line = f"{self.ast[index + 1]['value']} = {self.ast[index + 3]['value']}"
                index += 5
            elif self.ast[index]['value'] == 'call': # Function call
                line = f"{self.ast[index + 1]['value']}({self.ast[index + 2]['value']})" # Function name and argument
                index += 4
            elif self.ast[index]['value'] == 'return': # Return statement
                line = f"return {self.ast[index + 1]['value']}"
                index += 3
            elif self.ast[index]['value'] == 'display': # Print statement
                display_args = [] # List of arguments to be printed
                index += 1
                while index < len(self.ast) and (self.ast[index]['type'] != 'operator' or self.ast[index]['value'] != ';'):
                    if self.ast[index]['type'] == 'string':
                        display_args.append(f'"{self.ast[index]["value"]}"')
                    elif self.ast[index]['type'] == 'variable':
                        display_args.append(self.ast[index]['value'])
                    index += 1
                
                line = f"print({', '.join(display_args)})"  # Print statement with all arguments
            elif self.ast[index]['value'] == 'while': # While loop (no else)
                condition = f"{self.ast[index + 1]['value']} {self.ast[index + 2]['value']} {self.ast[index + 3]['value']}"
                line = f"while {condition}:"
                index += 5
                indent = "  " # Indentation for the while loop
                while index < len(self.ast) and (self.ast[index]['type'] != 'keyword' or self.ast[index]['value'] != 'endwhile'):
                    sub_line = self.line_processor(index)
                    if sub_line:
                        line += "\n" + indent + sub_line
                    index += 1
            elif self.ast[index]['value'] == 'if': # If statement (with else)
                condition = f"{self.ast[index + 1]['value']} {self.ast[index + 2]['value']} {self.ast[index + 3]['value']}"
                line = f"if {condition}:"
                index += 5
                indent = "  "
                while index < len(self.ast) and (self.ast[index]['type'] != 'keyword' or self.ast[index]['value'] != 'endif'):
                    sub_line = self.line_processor(index)
                    if sub_line:
                        line += "\n" + indent + sub_line
                    index += 1
            elif self.ast[index]['value'] == 'else': # Else statement
                line = f"else:"
                index += 2
                indent = "  "
                while index < len(self.ast) and (self.ast[index]['type'] != 'keyword' or self.ast[index]['value'] != 'endelse'):
                    sub_line = self.line_processor(index)
                    if sub_line:
                        line += "\n" + indent + sub_line
                    index += 1

        return line # Return the line


if __name__ == "__main__":
    lexer = Lexer() # Create a lexer object
    tokens = lexer.lex("test_SCL.scl") # Lex the file
    print("Tokens:", [token.value for token in tokens]) # Print the tokens

    parser = Parser(tokens) # Create a parser object
    ast = parser.parse()    # Parse the tokens
    print("AST:", json.dumps(ast, indent=2)) # Print the AST

    executor = Executor(ast) # Create an executor object
    result = executor.execute() # Execute the AST

    with open("output.py", "w") as output_file: # Write the Python code to a file
        output_file.write(result) # Write the Python code to the file
    print("Python code has been written to output.py") # Print a message to the user