package com.nigoote.peaplesafetyapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    Button logout;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
//        logout = (Button) findViewById(R.id.logoutbtn);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(getResources().getString(R.string.prefLoginState),"loggedout");
//                editor.apply();
//                startActivity((new Intent(HomeActivity.this,LoginActivity.class)));
//                finish();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.editProfilemenu:
                Toast.makeText(this, "Edit Profile Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Profilemenu:
                Toast.makeText(this, "Your Profile Clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.editEmergencymenu:
                Toast.makeText(this, "Edit Emergency Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logoutmenu:
                final SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getResources().getString(R.string.prefLoginState),"loggedout");
                editor.apply();
                startActivity((new Intent(HomeActivity.this,LoginActivity.class)));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}