package com.nigoote.peaplesafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
     TextView NewAccountLink;
     Button login;
     CheckBox loginState;
     EditText PhoneNumberLg,PasswordLg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PhoneNumberLg = (EditText) findViewById(R.id.txtphonelg);
        PasswordLg = (EditText) findViewById(R.id.txtpasslg);
        loginState = (CheckBox) findViewById(R.id.checkBox);

        NewAccountLink = (TextView) findViewById(R.id.newaccountlink);
        NewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentnew = new Intent(LoginActivity.this,CreateNewAcount.class);
                startActivity(intentnew);
                finish();
            }
        });
    }
}