package com.example.inclass07;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inclass07.DataModel.Notes;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {


    ArrayList<Notes.Note> notes;

    public NotesAdapter(ArrayList<Notes.Note> notes) {
        this.notes = notes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private String BASE_URL = "http://ec2-54-164-201-39.compute-1.amazonaws.com:3000/api/note";

        private final TextView noteId;
        private final TextView note;
        private final CardView cardView;
        private final View v;
        private final Button deleteBtn;

        public OkHttpClient getClient() {
            return client;
        }

        private OkHttpClient client = new OkHttpClient();

        public String getBASE_URL() {
            return BASE_URL;
        }


        public Handler getHandler() {
            return handler;
        }

        private Handler handler = new Handler(Looper.getMainLooper());
        public Button getDeleteBtn() {
            return deleteBtn;
        }



        public View getView() {
            return v;
        }

        public TextView getNoteId() {
            return noteId;
        }

        public TextView getNote() {
            return note;
        }

        public CardView getCardView() {
            return cardView;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteId =itemView.findViewById(R.id.noteTitle);
            note = itemView.findViewById(R.id.noteText);
            cardView = itemView.findViewById(R.id.main_container);
            deleteBtn = itemView.findViewById(R.id.deletebtn);
            v = itemView;
        }
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notesrecycler,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
            holder.getNote().setText(notes.get(position).getText());
            holder.getNoteId().setText(notes.get(position).get_id());
            holder.getDeleteBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RequestBody formBody = new FormBody.Builder()
                            .add("id", notes.get(position).get_id())
                            .build();

                    SharedPreferences sharedPref = ((AppCompatActivity) holder.getNoteId().getContext()).getSharedPreferences("data", Context.MODE_PRIVATE);
                    String t = sharedPref.getString(((AppCompatActivity) holder.getNoteId().getContext()).getString(R.string.login_data),"empty");
                    Log.d("token string", "onClick: " + t);

                    Request request = new Request.Builder()
                            .url(holder.getBASE_URL() + "/delete")
                            .addHeader("x-access-token", sharedPref.getString(((AppCompatActivity) holder.getNoteId().getContext()).getString(R.string.login_data),"empty"))
                            .post(formBody)
                            .build();

                    holder.getClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                            Log.d("fail", "onFailure: " + "failiure");
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if(response.isSuccessful()){
                                ResponseBody responseBody =response.body();
                                Log.d("delete msg", "onResponse: " + responseBody.string());
                                holder.getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {

                                        ((AppCompatActivity) holder.getNoteId().getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.rootlayout, new NotesDisplay(), "newnotetodisplaynote").commit();
                                    }});

                            }

                            else{
                                Log.d("delete msg unsuccess", "onResponse: " + response.body().string());

                            }

                        }
                    });



                }


            });
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("noteVal", "onClick: "+notes.get(position).get_id());
                ((AppCompatActivity) holder.getNoteId().getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.rootlayout, com.example.inclass07.Notes.newInstance(notes.get(position).get_id(), notes.get(position).getText())).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
