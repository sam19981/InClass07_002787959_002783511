package com.example.inclass07;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inclass07.DataModel.Notes;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {


    ArrayList<Notes.Note> notes;

    public NotesAdapter(ArrayList<Notes.Note> notes) {
        this.notes = notes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView noteId;
        private final TextView note;
        private final CardView cardView;
        private final View v;

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
