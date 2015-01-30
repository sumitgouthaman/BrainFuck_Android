package com.sumitgouthaman.brainfuck_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class CodeActivity extends ActionBarActivity {

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

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CodeFragment extends Fragment {

        final String TAG = getClass().getSimpleName();

        TextView codeView;
        Button buttonLeft, buttonRight,
                buttonIncrement, buttonDecrement,
                buttonStartBracket, buttonStopBracket,
                buttonPrintChar, buttonReadChar;
        Button buttonNext, buttonSpace, buttonMoveCursorLeft, buttonMoveCursorRight, buttonDel;

        public CodeFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_code, container, false);
            codeView = (TextView) rootView.findViewById(R.id.textView_codeView);
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
            int cursorPos = currentCode.indexOf(Constants.cursor);
            String preString = currentCode.substring(0, cursorPos);
            String postString = currentCode.substring(cursorPos);
            currentCode = preString + token + postString;
            codeView.setText(currentCode);
        }

        private void moveCursor(Constants.Direction direction) {
            String currentCode = codeView.getText().toString();
            int cursorPos = currentCode.indexOf(Constants.cursor);
            String preString = currentCode.substring(0, cursorPos);
            String postString;
            if (currentCode.indexOf(Constants.cursor) == currentCode.length() - 1) {
                postString = "";
            } else {
                postString = currentCode.substring(cursorPos + 1);
            }
            if (direction == Constants.Direction.LEFT) {
                int preStringLen = preString.length();
                if (preStringLen == 0) return;
                char last = preString.charAt(preStringLen - 1);
                preString = preString.substring(0, preStringLen - 1);
                postString = last + postString;
            } else if (direction == Constants.Direction.RIGHT) {
                int postStringLen = postString.length();
                if (postStringLen == 0) return;
                char first = postString.charAt(0);
                postString = postString.substring(1);
                preString = preString + first;
            }
            currentCode = preString + '|' + postString;
            codeView.setText(currentCode);
        }

        private void backSpace(){
            String currentCode = codeView.getText().toString();
            int cursorPos = currentCode.indexOf(Constants.cursor);
            String preString = currentCode.substring(0, cursorPos);
            int preStringLen = preString.length();
            if (preStringLen == 0) return;
            String postString;
            if (currentCode.indexOf(Constants.cursor) == currentCode.length() - 1) {
                postString = "";
            } else {
                postString = currentCode.substring(cursorPos + 1);
            }
            preString = preString.substring(0, preStringLen - 1);
            currentCode = preString + '|' + postString;
            codeView.setText(currentCode);
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
