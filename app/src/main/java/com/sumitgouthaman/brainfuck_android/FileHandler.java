package com.sumitgouthaman.brainfuck_android;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
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

    public String openFile(String filename) {
        File textFile = new File(folder, filename);
        FileInputStream fis;
        try {
            fis = new FileInputStream(textFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fis = null;
        }
        StringBuffer content = new StringBuffer("");

        byte[] buffer = new byte[1024];
        int n;
        try {
            while((n = fis.read(buffer)) != -1) {
                content.append(new String(buffer, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
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

    public String[] fileList() {
        String[] fileList;
        if(folder.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    return filename.contains(".txt") || sel.isDirectory();
                }
            };
            fileList = folder.list(filter);
        }
        else {
            fileList = null;
        }
        return fileList;
    }
}
