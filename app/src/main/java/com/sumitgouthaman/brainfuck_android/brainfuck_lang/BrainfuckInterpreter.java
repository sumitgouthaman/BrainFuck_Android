package com.sumitgouthaman.brainfuck_android.brainfuck_lang;

import com.sumitgouthaman.brainfuck_android.Constants;

import java.util.ArrayList;

/**
 * Created by sumit on 1/16/15.
 */
public class BrainfuckInterpreter {
    public static byte[] interpret(String code, byte[] input) {
        int inputPos = 0;
        ArrayList<Byte> outputList = new ArrayList<Byte>();
        char[] program = code.toCharArray();
        int progPos = 0;
        int progLen = program.length;

        byte[] cells = new byte[30000];
        int cellPos = 0;

        while (progPos < progLen) {
            switch (program[progPos]) {
                case Constants.left:
                    cellPos--;
                    progPos++;
                    break;
                case Constants.right:
                    cellPos++;
                    progPos++;
                    break;
                case Constants.increment:
                    cells[cellPos]++;
                    progPos++;
                    break;
                case Constants.decrement:
                    cells[cellPos]--;
                    progPos++;
                    break;
                case Constants.printChar:
                    outputList.add(cells[cellPos]);
                    progPos++;
                    break;
                case Constants.readChar:
                    cells[cellPos] = input[inputPos++];
                    progPos++;
                    break;
                case Constants.startBracket:
                    if (cells[cellPos] == 0) {
                        while (program[progPos] != Constants.stopBracket) {
                            progPos++;
                        }
                        progPos++;
                    } else {
                        progPos++;
                    }
                    break;
                case Constants.stopBracket:
                    if (cells[cellPos] != 0) {
                        while (program[progPos] != Constants.startBracket) {
                            progPos--;
                        }
                        progPos++;
                    } else {
                        progPos++;
                    }
            }
        }

        byte[] output = new byte[outputList.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = outputList.get(i);
        }

        return output;
    }

    public static String interpret(String code, String input) {
        byte[] inputBytes = input.getBytes();
        byte[] output = interpret(code, inputBytes);
        return new String(output);
    }
}
