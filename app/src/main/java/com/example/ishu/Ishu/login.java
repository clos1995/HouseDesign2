package com.example.ishu.Ishu;

import android.os.Bundle;

import com.example.manju.housedesign.R;

public class login extends AppCompactActivity{

private EditText Name;
    private EditText Password;
    private TextView info;
    private Button Login;

protected void oncreate(Bundle savedInstanceState)
 super.oncreate(savedInstanceState);
 setContentView(R.Layout.activity_main)
     Name = (EditText)findviewById(R.id.etname);
    Password = (EditText)findViewBYId(R.id.etpass);
    info = (TextView)findById(R.id.info);
    Login = (Button)findviewByid(R.id.btn);



