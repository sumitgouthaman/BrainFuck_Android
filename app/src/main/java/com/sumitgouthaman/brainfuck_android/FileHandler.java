package com.sumitgouthaman.brainfuck_android;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by David Baines on 05-Aug-16.
 */
public class FileHandler {

    Context context;
    File folder;

    public FileHandler(Context c) {

        //Creates a default directory for saving if it doesn't already exist

        // this should work but doesnt
        folder = new File(Environment.getExternalStorageDirectory().toString() + "/BrainFk");
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                Log.d("Brainfuck", "Directory Creation Failed, loc: " + folder.getAbsolutePath());
            }
        } else {
            Log.d("Brainfuck", "Directory Created at: " + folder.getAbsolutePath());
        }

        context = c;
    }

    public boolean saveFile(String text, String filename) {
        File textFile = new File(folder, filename);
        try {
            textFile.delete();
        } catch(Exception e) {

        }
        textFile = new File(folder, filename);
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(textFile);
            outputStream.write(text.getBytes());
            outputStream.close();

            Toast fold = Toast.makeText(context, "Saving File at: " + textFile.getPath(), Toast.LENGTH_SHORT);
            fold.show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
