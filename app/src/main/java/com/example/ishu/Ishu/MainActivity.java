package com.example.ishu.Ishu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.manju.housedesign.R;

public class MainActivity extends AppCompatActivity {

    Button done;
    EditText height,width,name;
    float x,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        done = (Button) findViewById(R.id.submit);
        height = (EditText) findViewById(R.id.height);
        width = (EditText) findViewById(R.id.width);
        name = (EditText)findViewById(R.id.name);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x = Integer.parseInt(String.valueOf(height.getText()));
                y = Integer.parseInt(String.valueOf(width.getText()));
                String nam = name.getText().toString();
                Intent i = new Intent(MainActivity.this,BasePlan.class);
                i.putExtra("name",nam);
                i.putExtra("height",x);
                i.putExtra("width",y);
                startActivity(i);
            }
        });

    }
}
