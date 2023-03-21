package com.example.inclass07.DataModel;

import java.util.ArrayList;

public class Notes {

    public static class Note
    {
        String _id;
        String text;

        public Note() {
        }

        public String get_id() {
            return _id;
        }

        public String getText() {
            return text;
        }

    }

    ArrayList<Note> notes;

    public Notes() {
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }
}
