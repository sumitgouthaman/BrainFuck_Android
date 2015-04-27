package com.sumitgouthaman.brainfuck_android.brainfuck_lang;

/**
 * Created by sumit on 2/9/15.
 */
public class ProgramException extends Exception {
    public boolean startOfCode;
    public ProgramException(boolean startOfCode) {
        this.startOfCode = startOfCode;
    }
}
