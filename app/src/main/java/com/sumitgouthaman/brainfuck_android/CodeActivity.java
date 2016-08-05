package com.sumitgouthaman.brainfuck_android;

import android.app.AlertDialog;
//import android.app.Fragment;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CodeActivity extends ActionBarActivity {

    String activeFile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CodeFragment())
                    .commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("BrainFuck", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_saveas) {
            EditText codeView = (EditText) findViewById(R.id.textView_codeView);
            String text = codeView.getText().toString();
            if(text != "") {
                filenameBox(text);
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "No Text Error", Toast.LENGTH_SHORT).show();
            }
        }
        if(id == R.id.action_save) {
            EditText codeView = (EditText) findViewById(R.id.textView_codeView);
            String text = codeView.getText().toString();
            if(text != "") {
                if(activeFile == "") {
                    filenameBox(text);
                } else {
                    FileHandler fh = new FileHandler(getApplicationContext());
                    fh.saveFile(codeView.getText().toString(), activeFile);
                }
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "No Text Error", Toast.LENGTH_SHORT).show();
            }
        }
        if(id == R.id.action_open) {
            FileHandler fh = new FileHandler(getApplicationContext());
            String[] fileList = fh.fileList();
            if(fileList != null) {
                openFileBox(fileList);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void openFileBox(String[] fileList) {
        //onClick doesn't like the parameter for some reason
        final String[] flist = fileList;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose your File:");
        builder.setItems(fileList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int index) {
                String chosenFile = flist[index];
                FileHandler fh = new FileHandler(getApplicationContext());
                String fileC = fh.openFile(chosenFile);
                activeFile = chosenFile;

                TextView fileMenu = (TextView) findViewById(R.id.filename_text);
                fileMenu.setText("File Name: " + chosenFile);

                CodeFragment.cursorPos = 0;

                EditText codeView = (EditText) findViewById(R.id.textView_codeView);
                codeView.setText(fileC);
            }
        });
        builder.show();
    }

    public void filenameBox(String text) {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        final View promptView = li.inflate(R.layout.file_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText userInput = (EditText) promptView.findViewById(R.id.filename_input);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditText codeView = (EditText) findViewById(R.id.textView_codeView);
                FileHandler fh = new FileHandler(getApplicationContext());
                fh.saveFile(codeView.getText().toString(), userInput.getText().toString() + ".txt");
                activeFile = userInput.getText().toString() + ".txt";

                TextView fileMenu = (TextView) findViewById(R.id.filename_text);
                fileMenu.setText("File Name: " + userInput.getText().toString() + ".txt");

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userInput.getWindowToken(), 0);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userInput.getWindowToken(), 0);
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CodeFragment extends Fragment {

        final String TAG = getClass().getSimpleName();

        static int cursorPos = 0;

        boolean leftHeld = false;
        boolean rightHeld = false;

        EditText codeView;
        Button buttonLeft, buttonRight,
                buttonIncrement, buttonDecrement,
                buttonStartBracket, buttonStopBracket,
                buttonPrintChar, buttonReadChar;
        Button buttonNext, buttonSpace, buttonMoveCursorLeft, buttonMoveCursorRight, buttonDel;
        Button buttonPaste;

        public CodeFragment() {
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_code, container, false);
            codeView = (EditText) rootView.findViewById(R.id.textView_codeView);
            buttonLeft = (Button) rootView.findViewById(R.id.button_left);
            buttonRight = (Button) rootView.findViewById(R.id.button_right);
            buttonIncrement = (Button) rootView.findViewById(R.id.button_increment);
            buttonDecrement = (Button) rootView.findViewById(R.id.button_decrement);
            buttonStartBracket = (Button) rootView.findViewById(R.id.button_start_bracket);
            buttonStopBracket = (Button) rootView.findViewById(R.id.button_stop_bracket);
            buttonPrintChar = (Button) rootView.findViewById(R.id.button_print_char);
            buttonReadChar = (Button) rootView.findViewById(R.id.button_read_char);
            buttonSpace = (Button) rootView.findViewById(R.id.button_space);
            buttonDel = (Button) rootView.findViewById(R.id.button_del);
            buttonMoveCursorLeft = (Button) rootView.findViewById(R.id.button_move_left);
            buttonMoveCursorRight = (Button) rootView.findViewById(R.id.button_move_right);
            buttonNext = (Button) rootView.findViewById(R.id.button_next);
            buttonPaste = (Button) rootView.findViewById(R.id.button_paste);

            //This purposefully doesn't do anything, it hopefully will stop the keyboard from popping up
            View.OnTouchListener otl = new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            };

            codeView.setOnTouchListener(otl);

            buttonLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertAtCursor(Constants.left);
                }
            });

            buttonRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertAtCursor(Constants.right);
                }
            });

            buttonIncrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertAtCursor(Constants.increment);
                }
            });

            buttonDecrement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertAtCursor(Constants.decrement);
                }
            });

            buttonStartBracket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertAtCursor(Constants.startBracket);
                }
            });

            buttonStopBracket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertAtCursor(Constants.stopBracket);
                }
            });

            buttonPrintChar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertAtCursor(Constants.printChar);
                }
            });

            buttonReadChar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertAtCursor(Constants.readChar);
                }
            });

            buttonSpace.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertAtCursor(Constants.space);
                }
            });

            buttonDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backSpace();
                }
            });

            buttonMoveCursorLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveCursor(Constants.Direction.LEFT);
                }
            });

            buttonMoveCursorRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveCursor(Constants.Direction.RIGHT);
                }
            });

            //Hold support later
            /*buttonMoveCursorLeft.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            leftHeld = true;

                            return false;
                        case MotionEvent.ACTION_UP:
                            leftHeld = false;
                            return false;
                    }
                    return false;
                }
            });*/

            /*buttonMoveCursorRight.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            rightHeld = true;
                            while(leftHeld) {
                                moveCursor(Constants.Direction.RIGHT);
                            }
                            return true;
                        case MotionEvent.ACTION_UP:
                            rightHeld = false;
                            return true;
                    }
                    return false;
                }
            });*/

            buttonPaste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    if (!(clipboard.hasPrimaryClip())) {
                        Toast.makeText(getActivity(), getString(R.string.no_text_in_clipboard), Toast.LENGTH_SHORT).show();
                    } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))){
                        Toast.makeText(getActivity(), getString(R.string.no_text_in_clipboard), Toast.LENGTH_SHORT).show();
                    } else {
                        ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                        String pasteData = item.getText().toString();
                        String currentCode = codeView.getText().toString();
                        int cursorPos = currentCode.indexOf(Constants.cursor);
                        String preString = currentCode.substring(0, cursorPos);
                        String postString = currentCode.substring(cursorPos);
                        currentCode = preString + " " + pasteData + " " + postString;
                        codeView.setText(currentCode);
                        Toast.makeText(getActivity(), getString(R.string.pasted_from_clipboard), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentCode = codeView.getText().toString();
                    String pureCode = pureCode(currentCode);
                    //Log.d(TAG, pureCode);
                    Intent intent = new Intent(getActivity(), InputOutputActivity.class);
                    intent.putExtra(InputOutputActivity.InputOutputFragment.KEY_PURECODE, pureCode);
                    startActivity(intent);
                }
            });

            return rootView;
        }

        private void insertAtCursor(char token) {
            String currentCode = codeView.getText().toString();
            String preString;
            try {
                preString = currentCode.substring(0, cursorPos);
            } catch(Exception e) {
                preString = "";
            }
            String postString;
            try {
                postString = currentCode.substring(cursorPos, codeView.length());
            } catch (Exception e) {
                postString = "";
            }
            currentCode = preString + token + postString;
            Log.d("Brainfuck", "Text: '" + currentCode + "' Cursor: " + cursorPos);
            codeView.setText(currentCode);
            cursorPos++;

            codeView.setSelection(cursorPos);
        }

        private void moveCursor(Constants.Direction direction) {
            if (direction == Constants.Direction.LEFT && cursorPos != 0) {
                cursorPos--;
                codeView.setSelection(cursorPos);
            } else if (direction == Constants.Direction.RIGHT && codeView.getText().length() != cursorPos) {
                cursorPos++;
                codeView.setSelection(cursorPos);
            }
        }

        private void backSpace(){
            if(cursorPos != 0) {
                String currentCode = codeView.getText().toString();
                cursorPos--;
                String preString = currentCode.substring(0, cursorPos);
                String postString;
                try {
                    postString = currentCode.substring(cursorPos + 1, codeView.length());
                } catch (Exception e) {
                    postString = "";
                }
                currentCode = preString + postString;
                codeView.setText(currentCode);
                codeView.setSelection(cursorPos);
            }
        }

        private String pureCode(String rawCode) {
            StringBuilder code = new StringBuilder();
            for (char c : rawCode.toCharArray()) {
                if (Constants.allowedChars.contains("" + c)) {
                    code.append(c);
                }
            }
            return code.toString();
        }
    }
}
