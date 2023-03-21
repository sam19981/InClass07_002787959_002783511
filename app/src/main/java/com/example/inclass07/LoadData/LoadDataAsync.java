package com.example.inclass07.LoadData;

import android.os.Handler;
import android.os.Looper;

import com.example.inclass07.DataModel.Notes;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadDataAsync {
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());
    private NetworkResponseListner networkResponseListner;
    private Notes allNotes;
    private OkHttpClient client = new OkHttpClient();
    private String baseUrl = "http://ec2-54-164-201-39.compute-1.amazonaws.com:3000/api/note/getall";
    private String authToken = "";




    public LoadDataAsync(String auth,NetworkResponseListner networkResponseListner) {
        authToken = auth;
        this.networkResponseListner = networkResponseListner;
    }

    public void execute()
    {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                HttpUrl httpUrlBuilder = HttpUrl.parse(baseUrl).newBuilder().build();

                Request request = new Request.Builder().url(httpUrlBuilder).addHeader("x-access-token", authToken).build();

                Response response;

                try
                {
                    response = client.newCall(request).execute();
                } catch(IOException e)
                {
                    throw new RuntimeException(e);
                }

                if(response !=null&&response.isSuccessful())
                {
                    try {
                        if (response.body() != null) {
                            Gson gsonParser = new Gson();
                            allNotes = gsonParser.fromJson(response.body().charStream(), Notes.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //UI Thread work here
                                    networkResponseListner.SuccessData(allNotes.getNotes());
                                }
                            });
                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //UI Thread work here
                                    networkResponseListner.FailedData();
                                }
                            });
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //UI Thread work here
                            networkResponseListner.FailedData();
                        }
                    });
                }
            }

        });

    }


}
