package com.example.mybaicizhan.XML;

import android.app.Person;

import com.example.mybaicizhan.Http.SentencePerDay;
import com.google.gson.Gson;

public class JsonReader {

    Gson gson;

    public JsonReader(){
        gson = new Gson();
    }

    public SentencePerDay parseJsonToSentencePerDay(String jsonData){
        SentencePerDay sentencePerDay = gson.fromJson(jsonData,SentencePerDay.class);
        sentencePerDay.printInfo();
        return sentencePerDay;
    }
}
