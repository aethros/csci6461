        ; Computer Architecture - CSCI 6461
        ; Program 1
        ;
        ; Basic Flow: 
        ; Read in 20 numbers
        ; Print numbers to console
        ; Searches numbers for closest entered by User
        ;
        ; Data:
        ; Memory Addresses	0 - 31 (Data storage)
        ; Program Prompts:
        ; "Please enter the Xth Number: "
        ; "Guess a number: "
        ; "The closest number is: "
        ;
        ; Program:
        ; Function to input a number (x 21)
        ; Store that number in memory
        ; Function to search address space
        ; Function to Print answer
        ;
        ; user types
        ; User hits enter
        ; For loop of input strings
        ; Pop off text as IN instruction is called
        ;
        ;
        ; Read in char
        ; For loop of text buffer
        ; Append char to textpage
        ; Add newline as special char
NUM20:  Data   0 ;
NUM19:  Data   0 ;
NUM18:  Data   0 ;
NUM17:  Data   0 ;
NUM16:  Data   0 ;
NUM15:  Data   0 ;
NUM14:  Data   0 ;
NUM13:  Data   0 ;
NUM12:  Data   0 ;
NUM11:  Data   0 ;
NUM10:  Data   0 ;
NUM09:  Data   0 ;
NUM08:  Data   0 ;
NUM07:  Data   0 ;
NUM06:  Data   0 ;
NUM05:  Data   0 ;
NUM04:  Data   0 ;
NUM03:  Data   0 ;
NUM02:  Data   0 ;
NUM01:  Data   0 ;
INUM:   Data   0 ;
LENT:   Data   ENTER ;
LGUE:   Data   GUESS ;
LANS:   Data   ANS ;
LLOO:   Data   LOOP ;
LINP:   Data   INPUT ;
LILO:   Data   ILOOP ;
LLKE:   Data   LKEY ;
R0:     Data   0 ;
R1:     Data   0 ;
R2:     Data   0 ;
R3:     Data   0 ;
        LOC   40 ; Set Data Location to 40
ENTER:  Data  80 ; P
        Data 108 ; l
        Data 101 ; e
        Data  97 ; a
        Data 114 ; s
        Data 101 ; e
        Data  32 ;' '
        Data 101 ; e
        Data 110 ; n
        Data 116 ; t
        Data 101 ; e
        Data 114 ; r
        Data  32 ;' '
        Data  97 ; a
        Data  32 ;' '
        Data  78 ; N
        Data 117 ; u
        Data 109 ; m
        Data  98 ; b
        Data 101 ; e
        Data 114 ; r
        Data  58 ;':'
        Data  32 ;' '
        Data   0 ;'NULL'
        LOC   80 ; Set Data Location to 80
GUESS:  Data  71 ; G
        Data 117 ; u
        Data 101 ; e
        Data 114 ; s
        Data 114 ; s
        Data  32 ;' '
        Data  97 ; a
        Data  32 ;' '
        Data 110 ; n
        Data 117 ; u
        Data 109 ; m
        Data  98 ; b
        Data 101 ; e
        Data 114 ; r
        Data  58 ;':'
        Data  32 ;' '
        Data   0 ;'NULL'
        LOC  120 ; Set Data Location to 120
ANS:    Data  84 ; T
        Data 104 ; h
        Data 101 ; e
        Data  32 ;' '
        Data  99 ; c
        Data 108 ; l
        Data 111 ; o
        Data 114 ; s
        Data 101 ; e
        Data 114 ; s
        Data 116 ; t
        Data  32 ;' '
        Data 110 ; n
        Data 117 ; u
        Data 109 ; m
        Data  98 ; b
        Data 101 ; e
        Data 114 ; r
        Data  32 ;' '
        Data 105 ; i
        Data 114 ; s
        Data  58 ;':'
        Data  32 ;' '
        Data   0 ;'NULL'
        LOC  140 ; Set Program Location to 140
        ;
        ;  Main Program 
        ;
        ; R0 : Input/Output/Return Value
        ; R1 : String Pointer
        ; R2 : Counter
        ; R3 : 
        ;
        AIR 2,15        ; Add 15 to Counter
        AIR 2,5         ; Add  6 to Counter (Eq 21)
        LDR 1,0,[PTR],0 ; Load the Pointer to the String into R1
LOOP:   STR 2,0,R2,0    ; Store Counter to Memory
        JSR             ; Call Input Function with  "ENTER"
        LDR 2,0,R2,0    ; Load Counter from Memory
        SIR 2,1         ; Sub 1 to Counter
        STR 0,0,NUM01,0 ; Store Return Address to Memory (Use counter offset)**
        JGE 2,LOOP,0    ; Jump back to loop until 0
        AIR 2,15        ; Add 15 to String Pointer <}
        AIR 2,15        ; Add 15 to String Pointer <(+40 to next string)
        AIR 2,10        ; Add 10 to String Pointer <}
        ---             ; Set function Arguments (Store R1,R2,R3) (Load R1,R2)
        JSR             ; Call Input Function with  "GUESS"
        ---             ; Store Return Value (Load R1,R2) (Store R0)
        JSR             ; Search for closest number
        AIR 2,15        ; Add 15 to String Pointer <}
        AIR 2,15        ; Add 15 to String Pointer <(+40 to next string)
        AIR 2,10        ; Add 10 to String Pointer <}
        ---             ; Move Return to R2
        JSR             ; Print Answer             ("ANS")
        ;
        ; Input Function
        ;
        ; R0 : Input/Output/Return Value
        ; R1 : Pointer to String
        ; R2 : 
        ; R3 : Return Address (PC+1)
        ;
INPUT:  LDR 0,0,[R1],1  ; Load R1 (Contents of Memory Address) into R0
        OUT 0,1         ; Print R0
        AIR 1,1         ; Add 1 to R1
        JGE 0,INPUT,0   ; Jump to back to loop R0 is 0 (Null terminator)
LKEY:   CHK 2,0         ; Check for more Input
        IN  0,0         ; Get Keyboard Input
        ---             ; Handle char
        JZ  0,LKEY,0    ; Jump if more Input
        ---             ; String to int
        STR 0,0,[ADD],0 ; Store R0 into address
        RFS ADD         ; Return from Subroutine (R0 eq location of return address)
        ;
        ; Search Function
        ;
        ; R0 : Input/Output/Return Value
        ; R1 : Pointer to String
        ; R2 : 
        ; R3 : Return Address (PC+1)
        ;
        RFS             ; Return from Subroutine



        



