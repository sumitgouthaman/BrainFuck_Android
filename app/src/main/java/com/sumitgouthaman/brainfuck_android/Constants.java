package com.sumitgouthaman.brainfuck_android;

/**
 * Created by sumit on 1/15/15.
 */
public class Constants {
    final public static char left = '<';
    final public static char right = '>';
    final public static char increment = '+';
    final public static char decrement = '-';
    final public static char startBracket = '[';
    final public static char stopBracket = ']';
    final public static char printChar = '.';
    final public static char readChar = ',';
    final public static char cursor = '|';
    final public static char space = ' ';
    final public static String allowedChars = "<>+-[].,";

    static enum Direction {
        LEFT,
        RIGHT
    };
}
