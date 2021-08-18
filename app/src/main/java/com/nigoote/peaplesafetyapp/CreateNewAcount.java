package com.nigoote.peaplesafetyapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class CreateNewAcount extends AppCompatActivity {

    EditText FirstName,LastName,PhoneNUmber,Pass,Addresses,Allergy;
    Button BtnNewAccount;
    String fname,lname,phone,password1,address,allergy;
    RequestQueue requestQueue;

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

        BtnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtfn= FirstName.getText().toString();
                String txtln= LastName.getText().toString();
                String txtph= PhoneNUmber.getText().toString();
                String txtpass= Pass.getText().toString();
                String txtadd= Addresses.getText().toString();
                String txtallergy= Allergy.getText().toString();

                if (TextUtils.isEmpty(txtfn) || TextUtils.isEmpty(txtln) || TextUtils.isEmpty(txtph)
                        || TextUtils.isEmpty(txtpass) || TextUtils.isEmpty(txtadd) || TextUtils.isEmpty(txtallergy)){
                    Toast.makeText(CreateNewAcount.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                }else{
                    registerNewAccount(txtfn,txtln,txtph,txtpass,txtadd,txtallergy);
                }


            }
        });

    }

    private void registerNewAccount(final String firstname,final String lastname,final String phonenumber,final String password,final String address,final String allergy){
        ProgressDialog progressDialog = new ProgressDialog(CreateNewAcount.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Registering New Account");
        progressDialog.show();
        String url ="http://192.168.20.225:8080/personsafety/register.php";
        RequestQueue requestQueue = Volley.newRequestQueue(CreateNewAcount.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Successfully_Registered")){
                    Log.d("enock",response+"working");
                    progressDialog.dismiss();
                    Toast.makeText(CreateNewAcount.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateNewAcount.this,LoginActivity.class));
                    finish();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(CreateNewAcount.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(CreateNewAcount.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                param.put("firstname",firstname);
                param.put("lastname",lastname);
                param.put("phonenumber",phonenumber);
                param.put("password",password);
                param.put("address",address);
                param.put("allergy",allergy);
                return param;
            }
        };

//        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MySingleton.getmInstance(CreateNewAcount.this).addToRequestQueue(request);
        requestQueue.add(request);

    }
}