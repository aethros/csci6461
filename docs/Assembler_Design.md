# Assembler Design

Inspiration:
https://www.d.umn.edu/~gshute/asm/assembler-organization.html

1. Pass 1 Analyzer
    - During pass 1, the input is read and memory addresses are assigned to program labels. Memory is allocated sequentially so that the pass 1 component can use a location counter.
    - For each input statement, this counter is incremented by the size of memory allocated.
    - Whenever a label is encountered, it is recorded in the symbol table. The address assigned is the value of the location counter at the beginning of the statement.

    1. Symbol Table
        - An assembly language program contains label definitions that mark the memory locations. The labels can be used elsewhere in the program to refer to the marked locations.
        - The symbol table is a simple table structure whose entries contain memory addresses keyed by the program labels. When the assembler determines the address for a label it adds an entry into the symbol table. When it encounters a use of the label it can look up the address in the symbol table.
        - Each symbol is stored into a hash table with a memory address and a symbol name.

2. Pass 2 Translator
    - The primary effort in pass 2 is translating instructions into machine code.
        ```text
            Assembly Instruction Format:
            <LABEL:> <OPCODE> <R,IX,OPERAND | LABEL,I> <;COMMENT>
        ```

    2. Scanner
        - The scanner reads instructions from the input and loads them into a buffer.
        - The scanner then parses the tokens from a line of input into values and returns the parsed instruction for a given line.
        - Each parsed instruction corresponds to a label, an opcode, and several operands.

## Tasks:
 - Build a user interface (file picker), settings, etc
    - XML
    - Controller
 - Pass arguments to an assembler function
    - Analyzer
    - Symbol Table
    - Translator
    - Scanner
 - Get output as listing file
    - Print to file
 - Get output as machine code
    - Load into program memory
 - Launch simulator
    - Function to load program/start simulator