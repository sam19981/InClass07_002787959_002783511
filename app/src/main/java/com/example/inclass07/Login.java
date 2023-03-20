package com.example.inclass07;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */

// hello
public class Login  extends Fragment  {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String BASE_URL = "http://ec2-54-164-201-39.compute-1.amazonaws.com:3000/api/auth";

    private OkHttpClient client = new OkHttpClient();
    private EditText email;

    private EditText password;

    private Button login_btn;

    private String email_value = "";
    private String password_vaue = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.lEmailId);
        password = view.findViewById(R.id.lPasswordId);
        login_btn = view.findViewById(R.id.registerId);

        login_btn.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v)  {
                                             RequestBody formBody = new FormBody.Builder()
                                                     .add("email", email.getText().toString())
                                                     .add("password", password.getText().toString())
                                                     .build();
                                             //Log.d("print", "onClick: " + email.getText().toString() + " " + password.getText().toString());

                                             Request request = new Request.Builder()
                                                     .url(BASE_URL + "/login")
                                                     .post(formBody)
                                                     .build();

                                             client.newCall(request).enqueue(new Callback() {
                                                 @Override
                                                 public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                     e.printStackTrace();
                                                     Log.d("fail", "onFailure: " + "failiure");


                                                 }

                                                 @Override
                                                 public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                    if(response.isSuccessful()){
                                                        ResponseBody responseBody =response.body();
                                                        Log.d("onresponse", "onResponse: " + responseBody.string());
                                                        System.out.println("success");
                                                    }
                                                    else{
                                                        Log.d("not success", "onResponse: " + response.body());
                                                    }


                                                 }
                                             });




                                         }
                                     }
        );

        return view;
    }
}