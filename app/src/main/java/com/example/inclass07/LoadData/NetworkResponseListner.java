package com.example.inclass07.LoadData;

import com.example.inclass07.DataModel.Notes;

import java.util.ArrayList;

public interface NetworkResponseListner {
    //Handle When We Recevie Success Data
    void SuccessData(ArrayList<Notes.Note> notes);

    //When We Received Fail Response
    void FailedData();

}
