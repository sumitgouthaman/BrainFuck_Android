package com.sumitgouthaman.brainfuck_android.brainfuck_lang;

import com.sumitgouthaman.brainfuck_android.Constants;

import java.util.ArrayList;

/**
 * Created by sumit on 1/16/15.
 */
public class BrainfuckInterpreter {
    public static byte[] interpret(String code, byte[] input) throws InputException, ProgramException, TapeException {
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
                    if(cellPos < 0) {
                        throw new TapeException(true);
                    }
                    break;
                case Constants.right:
                    cellPos++;
                    progPos++;
                    if(cellPos >= cells.length){
                        throw new TapeException(false);
                    }
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
                    if (inputPos >= input.length) {
                        throw new InputException();
                    }
                    cells[cellPos] = input[inputPos++];
                    progPos++;
                    break;
                case Constants.startBracket:
                    if (cells[cellPos] == 0) {
                        while (program[progPos] != Constants.stopBracket) {
                            progPos++;
                            if(progPos >= program.length) {
                                throw new ProgramException(false);
                            }
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
                            if(progPos < 0) {
                                throw new ProgramException(true);
                            }
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

    public static String interpret(String code, String input) throws InputException, ProgramException, TapeException {
        byte[] inputBytes = input.getBytes();
        byte[] output = interpret(code, inputBytes);
        return new String(output);
    }
}
