package com.sumitgouthaman.brainfuck_android;

/**
 * Created by sumit on 1/15/15.
 */
public class Constants {
    final static char left = '<';
    final static char right = '>';
    final static char increment = '+';
    final static char decrement = '-';
    final static char startBracket = '[';
    final static char stopBracket = ']';
    final static char printChar = '.';
    final static char readChar = ',';
    final static char cursor = '|';
    final static char space = ' ';
    final static String allowedChars = "<>+-[].,";

    static enum Direction {
        LEFT,
        RIGHT
    };
}
