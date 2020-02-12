package com.example.ishu.Ishu;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manju.housedesign.R;

public class BasePlan extends AppCompatActivity {
    static SharedPreferences prefs;
    static SharedPreferences.Editor editor;
    static int i;
    static String nam;
    static float height,width;
    TextView resultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        i=0;
        height = getIntent().getExtras().getFloat("height");
        width = getIntent().getExtras().getFloat("width");
        nam = getIntent().getExtras().getString("name");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        editor.clear();
        editor.putString("name",nam);
        editor.putFloat("height",height);
        editor.putFloat("width",width);
        editor.commit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_plan);
        //Toast.makeText(this, "(" + height + "," + width + ")", Toast.LENGTH_SHORT).show();

        Button button = (Button) findViewById(R.id.alert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });
    }

        protected void showInputDialog() {

            // get prompts.xml view
            LayoutInflater layoutInflater = LayoutInflater.from(BasePlan.this);
            View promptView = layoutInflater.inflate(R.layout.inputlayout, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BasePlan.this);
            alertDialogBuilder.setView(promptView);

            final Spinner type = (Spinner) promptView.findViewById(R.id.rooms);
            final EditText height = (EditText) promptView.findViewById(R.id.editText);
            final EditText width = (EditText) promptView.findViewById(R.id.editText2);
            // setup a dialog window
            alertDialogBuilder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            editor.putString("Rect"+i+"t",type.getSelectedItem().toString());
                            editor.putInt("Rect"+i+"h",Integer.parseInt(height.getText().toString()));
                            editor.putInt("Rect"+i+"w",Integer.parseInt(width.getText().toString()));
                            editor.commit();
                            i++;
                            Toast.makeText(BasePlan.this, String.valueOf(i), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            // create an alert dialog
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();
        }

    }
