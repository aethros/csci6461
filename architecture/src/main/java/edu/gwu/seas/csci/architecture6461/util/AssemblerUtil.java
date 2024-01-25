package edu.gwu.seas.csci.architecture6461.util;

public class AssemblerUtil {  // TODO: implement AssemblerUtil
    
    /**
     * Converts a decimal number to a octal number.
     * 
     * @param decimal The decimal number to convert.
     * @return The octal value of the number.
     */
    public int decimalToOctal(int decimal) {
        int octal = 0;
        int i = 1;
        while (decimal != 0) {
            octal += (decimal % 8) * i; // Add modulo remainder to `octal`
            decimal /= 8;               // Divide `decimal` by 8
            i *= 10;                    // Multiply `i` by 10's place
        }
        return octal;
    }
}