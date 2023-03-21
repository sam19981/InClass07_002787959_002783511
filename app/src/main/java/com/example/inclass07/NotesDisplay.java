package com.example.inclass07;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.inclass07.DataModel.Notes;
import com.example.inclass07.LoadData.LoadDataAsync;
import com.example.inclass07.LoadData.NetworkResponseListner;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesDisplay#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesDisplay extends Fragment implements NetworkResponseListner {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button addBtn;

    private RecyclerView mRecyclerView;
    private NotesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotesDisplay() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesDisplay.
     */
    // TODO: Rename and change types and number of parameters
    public static NotesDisplay newInstance(String param1, String param2) {
        NotesDisplay fragment = new NotesDisplay();
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
        View view = inflater.inflate(R.layout.fragment_notes_display, container, false);

        addBtn = view.findViewById(R.id.addbtn);

        mRecyclerView = view.findViewById(R.id.recyclerViewId);
        //SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(
                "data", Context.MODE_PRIVATE);
        String logintoken = sharedPref.getString(getString(R.string.login_data), "empty");
        Log.d("token", "logintoken: " + logintoken);


        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.rootlayout,new NotesDisplay(), "login").commit();

            }
        });


        LoadDataAsync loadDataAsync = new LoadDataAsync(logintoken,this);
        loadDataAsync.execute();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.rootlayout, new CreateNote(), "displaytocreate").commit();
            }
        });


        return view;
    }


    @Override
    public void SuccessData(ArrayList<Notes.Note> notes) {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NotesAdapter(notes);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void FailedData() {
        Toast.makeText(getContext(), "Could not load Data", Toast.LENGTH_SHORT).show();
    }
}