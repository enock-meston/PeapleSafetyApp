package com.nigoote.peaplesafetyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateNewAcount extends AppCompatActivity {

    EditText FirstName,LastName,PhoneNUmber,Pass,Addresses,Allergy;
    Button BtnNewAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_acount);

        FirstName = (EditText) findViewById(R.id.edtfname);
        LastName = (EditText) findViewById(R.id.edtlname);
        PhoneNUmber = (EditText) findViewById(R.id.editTextPhone);
        Pass = (EditText) findViewById(R.id.editTextTextPassword);
        Addresses = (EditText) findViewById(R.id.Address);
        Allergy = (EditText) findViewById(R.id.Allergy);
        BtnNewAccount = (Button) findViewById(R.id.savebutton);


    }
}