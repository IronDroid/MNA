package com.gomedia.mna;

import java.lang.reflect.Field;

import com.gomedia.mna.tools.FileIO;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CodeMNA_Activity extends Activity {

    TextView textViewinCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_mna);

        textViewinCode = (TextView) findViewById(R.id.editText_inCode);

        Log.e("fer", "" + getSmallestScreenWidthDp(this));

    }

    public static int getSmallestScreenWidthDp(Context context) {
        Resources resources = context.getResources();
        try {
            Field field = Configuration.class.getDeclaredField("smallestScreenWidthDp");
            return (Integer) field.get(resources.getConfiguration());
        } catch (Exception e) {
            // not perfect because reported screen size might not include status and
            // button bars
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            int smallestScreenWidthPixels = Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
            return Math.round(smallestScreenWidthPixels / displayMetrics.density);
        }
    }

    public void button_1(View view) {
        textViewinCode.setText(textViewinCode.getText().toString() + ((TextView) view).getText());
    }

    public void button_2(View view) {
        textViewinCode.setText(textViewinCode.getText().toString() + ((TextView) view).getText());
    }

    public void button_3(View view) {
        textViewinCode.setText(textViewinCode.getText().toString() + ((TextView) view).getText());
    }

    public void button_4(View view) {
        textViewinCode.setText(textViewinCode.getText().toString() + ((TextView) view).getText());
    }

    public void button_5(View view) {
        textViewinCode.setText(textViewinCode.getText().toString() + ((TextView) view).getText());
    }

    public void button_6(View view) {
        textViewinCode.setText(textViewinCode.getText().toString() + ((TextView) view).getText());
    }

    public void button_7(View view) {
        textViewinCode.setText(textViewinCode.getText().toString() + ((TextView) view).getText());
    }

    public void button_8(View view) {
        textViewinCode.setText(textViewinCode.getText().toString() + ((TextView) view).getText());
    }

    public void button_9(View view) {
        textViewinCode.setText(textViewinCode.getText().toString() + ((TextView) view).getText());
    }

    public void button_0(View view) {
        textViewinCode.setText(textViewinCode.getText().toString() + ((TextView) view).getText());
    }

    public void button_Back(View view) {
            String s = textViewinCode.getText().toString();
            if (s.length() > 1) {
                textViewinCode.setText(s.substring(0, s.length() - 1));
            } else{
                textViewinCode.setText("");
            }
    }

    public void button_Guion(View view) {
    }

    public void buttonback(View view) {
        onBackPressed();
    }

    public void buttonNext(View view) {
        if (textViewinCode.getText().toString().length() != 0) {
            EditText editText = (EditText) findViewById(R.id.editText_inCode);

            GlobalDataApplication global = (GlobalDataApplication) getApplication();
            global.codeObra = editText.getText().toString();

            boolean bool = false;
            try {
                bool = FileIO.validateConnetDirfromSD(
                        "/.mna/",
                        new String[]{".jpg", "_" + global.idLang + ".txt"},
                        global.codeObra);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }
            if (bool) {
                startActivity(new Intent(this, PropertiesMNA_Activity.class).putExtra("tipo", 1));
            } else {
                Toast.makeText(this, getResources().getString(R.string.txt_erro_code), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.txt_in_code), Toast.LENGTH_LONG).show();
        }

    }
}
