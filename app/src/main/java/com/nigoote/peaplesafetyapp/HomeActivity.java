package com.nigoote.peaplesafetyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    Button logout;
    TextView viewPhone;
    ImageView LocationImage,PoliceHelpLineImage,VoiceImage,imagePlay;
    TextToSpeech textToSpeech;

    public static final String PHONE="PHONE";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPhone = (TextView) findViewById(R.id.viewPhonetxt);
        LocationImage = (ImageView) findViewById(R.id.imgLocation);
        PoliceHelpLineImage = (ImageView) findViewById(R.id.imgpolicehelp);
        VoiceImage = (ImageView) findViewById(R.id.imgvoice);
        imagePlay = (ImageView) findViewById(R.id.playsound);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    // select language
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

//        Location Button
        LocationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Your Location Clicked", Toast.LENGTH_SHORT).show();
            }
        });
//        ends Location Button

//        VoiceImage
        PoliceHelpLineImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = "112";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
//        end VoiceImage

//        Police Button
        VoiceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = getIntent();
                String extraPhoneNumber = intent1.getStringExtra(PHONE);
                sendVoiceToHeadquater(extraPhoneNumber);
//                Toast.makeText(HomeActivity.this, "Your Voice Clicked", Toast.LENGTH_SHORT).show();
            }
        });
//        ends Police Button

//        play my sound by click image
        imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Edittext Vlaue
//                Intent intent1 = getIntent();
//                String voice = intent1.getStringExtra("voice");
//                Intent intent2 = getIntent();
//                String extraPhoneNumber = intent2.getStringExtra(PHONE);
                playMethod();
//                int speech= textToSpeech.speak(voice,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

//        ends of play my sound by click image


        final SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        Intent intent1 = getIntent();
        String extraPhoneNumber = intent1.getStringExtra(PHONE);
        Log.e("enock","ee"+extraPhoneNumber);
        viewPhone.setText("Number is "+extraPhoneNumber);
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

    private void sendVoiceToHeadquater(final String phone1){
        ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Send your Voive Allergy to HeadQuater");
        progressDialog.show();
        String url ="http://192.168.20.170:8080/personsafety/askforhelp.php";
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("your_Voice_sent")){
//                    Log.d("enock",response+"working");
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                param.put("phonenumber", phone1);
                return param;
            }
        };
        requestQueue.add(request);
    } // end fo


//    ====================
//    ====================

    //select and play voice with method

    private void playMethod(){
        ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Playing My Voice");
        progressDialog.show();
        String url ="http://192.168.20.170:8080/personsafety/selectVoice.php";
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonarray = jsonobject.getJSONArray("Allergy");
                    JSONObject data = jsonarray.getJSONObject(0);
                    String sound = data.getString("Allergy");
                    int speech= textToSpeech.speak(sound,TextToSpeech.QUEUE_FLUSH,null);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(HomeActivity.this, "Voice not played", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                Intent intent1 = getIntent();
                String extraPhoneNumber = intent1.getStringExtra(PHONE);
                param.put("phonenumber",extraPhoneNumber);
                return param;
            }
        };
        requestQueue.add(request);
    }

}