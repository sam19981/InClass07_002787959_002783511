package com.example.inclass07;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    private String BASE_URL = "http://ec2-54-164-201-39.compute-1.amazonaws.com:3000/api/auth/me";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.rootlayout,new Login(), "login").commit();

    }
    @Override
    protected void onStart() {

        super.onStart();
        SharedPreferences sharedPref = getSharedPreferences(
                "data", Context.MODE_PRIVATE);
        String logintoken = sharedPref.getString(getString(R.string.login_data), "empty");
        if(!logintoken.equals("empty")){
            Request request = new Request.Builder()
                    .url(BASE_URL)
                    .addHeader("x-access-token", logintoken)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    Log.d("fail", "onFailure: " + "failiure");


                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {

                        Log.d("login data", "onResponse: " + R.string.login_data);
                    } else {
                        Log.d("not success", "onResponse: " + response.body());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "session timed out",Toast.LENGTH_SHORT).show();
                                        getSupportFragmentManager().beginTransaction().add(R.id.rootlayout,new Login(), "login").commit();

                                    }
                                });


                    }


                }

            });
        }
    }
}