package com.example.inclass07;

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
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

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
 * Use the {@link signUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signUp extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final OkHttpClient client = new OkHttpClient();

    private Handler handler = new Handler(Looper.getMainLooper());
    private EditText name;
    private EditText email;
    private EditText password;
    private Button signUp;

    private final String baseUrl = "http://ec2-54-164-201-39.compute-1.amazonaws.com:3000/api/auth/register";

    private String email_value = "";
    private String name_value ="";

    private String password_value = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment signUp.
     */
    // TODO: Rename and change types and number of parameters
    public static signUp newInstance(String param1, String param2) {
        signUp fragment = new signUp();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        name = view.findViewById(R.id.lEmailId);
        email = view.findViewById(R.id.lEmailId);
        password = view.findViewById(R.id.lPasswordId);
        signUp = view.findViewById(R.id.registerId);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_value = String.valueOf(name.getText());
                email_value = String.valueOf(email.getText());
                password_value = String.valueOf(password.getText());
                if (!name_value.equals("") && !email_value.equals("") && !password_value.equals(""))
                {
                    register();
                }
                else if(name_value.equals(""))
                {
                    Toast.makeText(getActivity(), "Name field cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(email_value.equals(""))
                {
                    Toast.makeText(getActivity(), "Email field cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else if(password.equals(""))
                {
                    Toast.makeText(getActivity(), "Password field cannot be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    void register()  {
        HttpUrl httpUrlbuilder = HttpUrl.parse(baseUrl).newBuilder().build();

        RequestBody formBody = new FormBody.Builder()
            .add("name",name_value)
                .add("email",email_value)
                .add("password",password_value)
            .build();

        Request request = new Request.Builder().url(httpUrlbuilder).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Registration Failed: "+e.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()) {

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "User Registered Successfully", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rootlayout,new Login(),"Login_from_signup").commit();
                        }
                    });
                }
                else
                {  ResponseBody body = response.body();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Registration Failed: "+response.code()+" "+response.message(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }



}