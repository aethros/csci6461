# Assembler Design

https://www.d.umn.edu/~gshute/asm/assembler-organization.html

1. Pass 1 Analyzer
    - During pass 1, the input is read and memory addresses are assigned to program labels. Memory is allocated sequentially so that the pass 1 component can use a location counter.
    - For each input statement, this counter is incremented by the size of memory allocated.
    - Whenever a label is encountered, it is recorded in the symbol table. The address assigned is the value of the location counter at the beginning of the statement.

    1. Symbol Table
        - An assembly language program contains label definitions that mark the memory locations. The labels can be used elsewhere in the program to refer to the marked locations.
        - The symbol table is a simple table structure whose entries contain memory addresses keyed by the program labels. When the assembler determines the address for a label it adds an entry into the symbol table. When it encounters a use of the label it can look up the address in the symbol table.
        - The status of a symbol has three flags:
            - F – the first occurrence of the symbol
            - A – already defined
            - D – a new definition

```text
   4’s (F)   2’s (A)   1’s (D)                  action
      0         0         0         symACT0: This is a forward reference.  Store LOC in VALUE field of the symbol table and return the previous contents of VALUE field
      0         0         1         symACT1: A previously used TOKEN is defined for the first time.  Store LOC in VALUE field of the symbol table and return the previous contents of VALUE field
      0         1         0         symACT2: A previously defined TOKEN is used.  Return the contents of VALUE field.
      0         1         1         symACT3: A previously defined TOKEN is defined again.  Print “Double Definition Error” and return -1 (0xFFFF)
      1         0         0         symACT4: TOKEN seen for the first time as a forward reference.  Store LOC in VALUE field and return 0xFFFF.
      1         0         1         symACT5: TOKEN seen for the first time as a definition.  Store LOC in VALUE field of the symbol table and return 0.
      1         1         0         N/A
      1         1         1         N/A
```

2. Pass 2 Translator
    - The primary effort in pass 2 is translating instructions into machine code.
        ```text
            Assembly Instruction Format:
            <LABEL:> <OPCODE> <R,IX,OPERAND | LABEL,I> <;COMMENT>
        ```

    2. Scanner
        - The scanner reads lines from the input and loads them into a buffer.
        - The scanner then combines the characters from a line of input into tokens and returns the **table of tokens** for a given line.
        - 5 types of tokens:
            - Number
            - Variable
            - Operator
            - Delimiter
            - End of line

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