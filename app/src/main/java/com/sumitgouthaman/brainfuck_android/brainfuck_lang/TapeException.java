package com.sumitgouthaman.brainfuck_android.brainfuck_lang;

/**
 * Created by sumit on 2/9/15.
 */
public class TapeException extends Exception {
    public boolean startOfTape;
    public TapeException(boolean startOfTape) {
        this.startOfTape = startOfTape;
    }
}
