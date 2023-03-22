package com.example.inclass07;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OkHttpClient client = new OkHttpClient();

    private Button back_btn;
    private String BASE_URL = "http://ec2-54-164-201-39.compute-1.amazonaws.com:3000/api/auth/me";

    private Handler handler = new Handler(Looper.getMainLooper());

    private TextView name;

    private TextView email;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    private class Me{
        private String _id;
        private String name;
        private String email;


        public String get_id() {
            return _id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        back_btn = view.findViewById(R.id.disp_back);
        name = view.findViewById(R.id.disp_name);
        email = view.findViewById(R.id.disp_email);


        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                "data", Context.MODE_PRIVATE);
        String logintoken = sharedPref.getString(getString(R.string.login_data), "empty");


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
                    ResponseBody responseBody = response.body();
                    //Log.d("onresponse", "onResponse: " + responseBody.string());
                    System.out.println("success");


                    // gson parsing
                    Gson gson = new Gson();
                    Me me = gson.fromJson(responseBody.string(), Me.class);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            name.setText(me.getName());
                            email.setText(me.getEmail());



                        }});



                    Log.d("login data", "onResponse: " + R.string.login_data);
                } else {
                    Log.d("not success", "onResponse: " + response.body());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "invalid credentials",Toast.LENGTH_SHORT).show();

                        }});

                }


            }

        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rootlayout, new NotesDisplay(), "newnotetodisplaynote").commit();
            }
        });



        return view;
    }
}