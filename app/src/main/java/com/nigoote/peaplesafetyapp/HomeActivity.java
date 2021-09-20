package com.nigoote.peaplesafetyapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.text.Html;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    TextView viewPhone;
    TextView locationView;
    ImageView LocationImage, PoliceHelpLineImage, VoiceImage, imagePlay;
    TextToSpeech textToSpeech;
    FusedLocationProviderClient fusedLocationProviderClient;

    public static final String PHONE = "PHONE";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        viewPhone = (TextView) findViewById(R.id.viewPhonetxt);
        locationView = (TextView) findViewById(R.id.txtmylocation);
        LocationImage = (ImageView) findViewById(R.id.imgLocation);
        PoliceHelpLineImage = (ImageView) findViewById(R.id.imgpolicehelp);
        VoiceImage = (ImageView) findViewById(R.id.imgvoice);
        imagePlay = (ImageView) findViewById(R.id.playsound);

//        initialize fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
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
                //ckech permission
                if (ActivityCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    when permision granted
                    getLocation();
//                    sendLocationMethod();
                } else {
                    //when permision Denied
                    ActivityCompat.requestPermissions(HomeActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
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


        final SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        Intent intent1 = getIntent();
        String extraPhoneNumber = intent1.getStringExtra(PHONE);
        Log.e("enock", "ee" + extraPhoneNumber);
        viewPhone.setText("Number is " + extraPhoneNumber);
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //initialize location
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(HomeActivity.this,
                                Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
//set latitude
                        locationView.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Latitude</b></font>" +
                                        addressList.get(0).getLatitude()
                        ));

                        sendLocationMethod();
                        sendLocationMethodMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    StrictMode.ThreadPolicy st = new StrictMode.ThreadPolicy.Builder().build();
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
        String url ="http://192.168.1.156:8080/personsafety/askforhelp.php";
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
        String url ="http://192.168.1.156:8080/personsafety/selectVoice.php";
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




//    =================

    //select and play voice with method

    private void sendLocationMethod(){
        ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Playing My Voice");
        progressDialog.show();
        String url ="http://192.168.1.156:8080/personsafety/selectGuadian.php";
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonarray = jsonobject.getJSONArray("GaudianPhoneNumber");
                    JSONObject data = jsonarray.getJSONObject(0);
                    String GaudianPhoneNumber = data.getString("GaudianPhoneNumber");
                    Toast.makeText(HomeActivity.this, "Message Location be sent To:"+GaudianPhoneNumber, Toast.LENGTH_SHORT).show();

//                    sendinggg sms with messageBird.com final MessageResponse response = messageBirdClient.sendMessage("+31XXXXXXXXX", "Hi! This is your first message", phones);

//                    end

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




//    send message


    private void sendLocationMethodMessage(){
        ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Playing My Voice");
        progressDialog.show();
        String url ="http://192.168.1.156:8080/personsafety/sendmessage.php";
        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonarray = jsonobject.getJSONArray("GaudianPhoneNumber");
                    JSONObject data = jsonarray.getJSONObject(0);
                    String GaudianPhoneNumber = data.getString("GaudianPhoneNumber");
                    Toast.makeText(HomeActivity.this, "Message Location be sent To:"+GaudianPhoneNumber, Toast.LENGTH_SHORT).show();
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