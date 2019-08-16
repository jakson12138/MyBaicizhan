package com.example.mybaicizhan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mybaicizhan.Db.MyDatabase;
import com.example.mybaicizhan.Http.MyOkHttp;
import com.example.mybaicizhan.Http.WordValue;
import com.example.mybaicizhan.XML.XMLReader;

public class WordValueActivity extends AppCompatActivity {

    private WordValueFragment wordValueFragment;
    private MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordvalue);

        wordValueFragment = (WordValueFragment) getSupportFragmentManager().findFragmentById(R.id.word_value_fragment);

        Intent intent = getIntent();
        String downloadXML = intent.getStringExtra("downloadXML");
        String word = intent.getStringExtra("word");
        if(downloadXML != null){
            XMLReader xmlReader = new XMLReader();
            WordValue wordValue = xmlReader.parseXMLWithPullForWord(downloadXML);
            wordValueFragment.refresh(wordValue);
        }
        else if(word != null){
            WordValue wordValue = queryByWord(word);
            if(wordValue != null){
                wordValueFragment.refresh(wordValue);
            }
        }
    }

    private WordValue queryByWord(String word){
        if(myDatabase == null){
            myDatabase = new MyDatabase(WordValueActivity.this);
        }
        return myDatabase.queryWordValueByWord(word);
    }

}
