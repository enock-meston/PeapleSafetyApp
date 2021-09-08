package com.nigoote.peaplesafetyapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String TEXT_TO_SENT ="phonenumber";
     TextView NewAccountLink;
     Button login;
     CheckBox loginState;
     EditText PhoneNumberLg,PasswordLg;
    SharedPreferences sharedPreferences;
    private String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        PhoneNumberLg = (EditText) findViewById(R.id.txtphonelg);
        PasswordLg = (EditText) findViewById(R.id.txtpasslg);
        loginState = (CheckBox) findViewById(R.id.checkBox);
        login = (Button) findViewById(R.id.loginbtn);
        NewAccountLink = (TextView) findViewById(R.id.newaccountlink);
        NewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentnew = new Intent(LoginActivity.this,CreateNewAcount.class);
                startActivity(intentnew);
                finish();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtph= PhoneNumberLg.getText().toString();
                String txtpass= PasswordLg.getText().toString();
                if (TextUtils.isEmpty(txtph) || TextUtils.isEmpty(txtpass)){
                    Toast.makeText(LoginActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                }else{
                    login(txtph,txtpass);
                }
            }
        });

        String loginStatus= sharedPreferences.getString(getResources().getString(R.string.prefLoginState),"");

        if (loginStatus.equals("loggedin")){
            String txtph= PhoneNumberLg.getText().toString();

        }
    }

    private void sendDate() {
        phone = PhoneNumberLg.getText().toString().trim();
        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
        intent.putExtra(HomeActivity.PHONE,phone);
        startActivity(intent);
    }

    private void login(final String phonenumber,final String password){
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Logging");
        progressDialog.show();
        String url ="http://192.168.20.170:8080/personsafety/login.php";
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Login_Success")){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (loginState.isChecked()){
                        editor.putString(getResources().getString(R.string.prefLoginState),"loggedin");
                    }else{
                        editor.putString(getResources().getString(R.string.prefLoginState),"loggedout");
                    }
                    editor.apply();
                    sendDate();
//                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                param.put("phonenumber",phonenumber);
                param.put("password",password);
                return param;
            }
        };
        requestQueue.add(request);
    }
}