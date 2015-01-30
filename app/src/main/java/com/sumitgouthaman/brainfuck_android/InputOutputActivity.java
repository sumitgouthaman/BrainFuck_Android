package com.sumitgouthaman.brainfuck_android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sumitgouthaman.brainfuck_android.brainfuck_lang.BrainfuckInterpreter;

import java.util.Arrays;


public class InputOutputActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_output);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new InputOutputFragment())
                    .commit();
        }
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_output, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class InputOutputFragment extends Fragment {

        final static String KEY_PURECODE = "PURECODE";
        final String TAG = getClass().getSimpleName();
        final static String KEY_INPUT = "INPUT";

        String code = null;
        Activity activity = null;

        EditText editText_Input;
        TextView textView_Output;
        Button button_Run;

        public InputOutputFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_input_output, container, false);

            activity = getActivity();

            code = activity.getIntent().getStringExtra(KEY_PURECODE);
            Log.d(TAG, code);

            editText_Input = (EditText) rootView.findViewById(R.id.editText_input);
            textView_Output = (TextView) rootView.findViewById(R.id.textView_output);

            button_Run = (Button) rootView.findViewById(R.id.button_run);
            button_Run.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputStr = editText_Input.getText().toString();
                    String output = BrainfuckInterpreter.interpret(code, inputStr);
                    textView_Output.setText(output);
                }
            });

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BrainFuck", MODE_PRIVATE);
            String inputStr = sharedPreferences.getString(KEY_INPUT, "");
            editText_Input.setText(inputStr);

            return rootView;
        }

        @Override
        public void onStop() {
            super.onStop();
            String inputStr = editText_Input.getText().toString();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("BrainFuck", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_INPUT, inputStr);
            editor.commit();
        }
    }
}
