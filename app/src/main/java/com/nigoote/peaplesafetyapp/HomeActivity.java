package com.nigoote.peaplesafetyapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    Button logout;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        getSupportActionBar().setTitle("Start Activity");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        logout = (Button) findViewById(R.id.logoutbtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getResources().getString(R.string.prefLoginState),"loggedout");
                editor.apply();
                startActivity((new Intent(HomeActivity.this,LoginActivity.class)));
                finish();
            }
        });

    }

}