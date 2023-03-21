package com.example.inclass07;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
 * Use the {@link CreateNote#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNote extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OkHttpClient client = new OkHttpClient();

    private TextView note;
    private Button notebtn;
    private Button backBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String BASE_URL = "http://ec2-54-164-201-39.compute-1.amazonaws.com:3000/api/note";


    public CreateNote() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNote.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateNote newInstance(String param1, String param2) {
        CreateNote fragment = new CreateNote();
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_create_note, container, false);
        note = view.findViewById(R.id.addnote);
        notebtn = view.findViewById(R.id.addnotebtn);
        backBtn = view.findViewById(R.id.backbtn);

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.rootlayout,new Login(), "login").commit();

            }
        });

        notebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody formBody = new FormBody.Builder()
                        .add("text", note.getText().toString())
                        .build();

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                Log.d("token string", "onClick: " + sharedPref.getString(getString(R.string.login_data),"empty"));

                Request request = new Request.Builder()
                        .url(BASE_URL + "/post")
                        .addHeader("x-access-token", sharedPref.getString(getString(R.string.login_data),"empty"))
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
                            Log.d("post msg", "onResponse: " + responseBody.string());
                            Toast.makeText(getContext(), "Note created Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.d("post msg unsuccess", "onResponse: " + response.body().string());

                        }

                    }
                });



            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back to list of notes
            }
        });


        return view;
    }
}